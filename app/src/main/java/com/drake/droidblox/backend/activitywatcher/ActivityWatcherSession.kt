package com.drake.droidblox.backend.activitywatcher

import android.content.Context
import android.util.Log
import com.drake.droidblox.DBApplication
import com.drake.droidblox.backend.NotificationHandler
import com.drake.droidblox.backend.activitywatcher.models.BSRpcMessage
import com.drake.droidblox.backend.activitywatcher.models.BSRpcSetRpcData
import com.drake.droidblox.backend.activitywatcher.models.LogEntries
import com.drake.droidblox.backend.apis.DiscordApi
import com.drake.droidblox.backend.apis.RobloxApi
import com.drake.droidblox.backend.apis.models.RobloxThumbnail
import com.drake.droidblox.backend.sharedprefs.playsession.models.PlaySession
import com.drake.droidblox.backend.suBinaryPath
import com.my.kizzyrpc.KizzyRPC
import com.my.kizzyrpc.model.Activity
import com.my.kizzyrpc.model.Assets
import com.my.kizzyrpc.model.Metadata
import com.my.kizzyrpc.model.Timestamps

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

private const val TAG = "DBActivityWatcher"

private const val activityWatcherScript = """#!/system/bin/sh
robloxPID=""
while [ -z "${'$'}robloxPID" ]; do
    robloxPID="$(pidof com.roblox.client)"
    if [ -z "${'$'}robloxPID" ]; then
        sleep 1
    fi
done

logcat --pid ${'$'}robloxPID &
logcatPID=$!

while true; do
    if [ -z "$(pidof com.roblox.client)" ]; then
        kill ${'$'}logcatPID
        exit 1;
    fi
    sleep 1
done"""
private const val DROIDBLOX_APPLICATION_ID = "1379313837169311825"

class ActivityWatcherSession(
    private val notifHandler: NotificationHandler,
    private val context: Context
) {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private var settingsManager = DBApplication.instance.settingsManager
    private var playSessionsManager = DBApplication.instance.playSessionsManager
    private lateinit var watcherJob: Job

    private lateinit var watcherProcess: Process
    private lateinit var processReader: BufferedReader

    private lateinit var rpcHandler: KizzyRPC
    private lateinit var currentActivity: Activity
    var lastRPCRequestTime: Long = 0

    var placeId: Long = 0
    var universeId: Long = 0
    var jobId: String = ""
    var udmuxIp: String = ""
    var userId: Long = 0
    var playedAt: Long = 0

    fun start() = runBlocking {
        if (settingsManager.showGameActivity && settingsManager.token != null) {
            Log.d(TAG, "Initializing KizzyRPC and setting activity")
            rpcHandler = KizzyRPC(settingsManager.token!!)
            rpcHandler.setActivity(
                Activity(
                    name = "Roblox",
                    timestamps = Timestamps(
                        start = System.currentTimeMillis()
                    ),
                    applicationId = DROIDBLOX_APPLICATION_ID
                ),
                since = 0,
                status = "online"
            )
        }
        Log.d(TAG, "Starting activity watcher")
        watcherJob = launch { startWatching() }
    }
    private suspend fun startWatching() {
        Log.d(TAG, "Writing watcher script")
        writeWatcherScript()

        Log.d(TAG, "Starting to monitor activity")
        watcherProcess = Runtime.getRuntime().exec("$suBinaryPath -c 'sh ${File(context.filesDir, "activity_watcher.sh").absolutePath}'")
        processReader = BufferedReader(InputStreamReader(watcherProcess.inputStream))

        processReader.useLines { logEntries ->
            logEntries.forEach { logEntry ->
                try {
                    if (LogEntries.GameJoiningEntry in logEntry) {
                        playedAt = System.currentTimeMillis()

                        val matchedRegex = LogEntries.GameJoiningEntryPattern.find(logEntry)
                        placeId = matchedRegex?.groups?.get(2)?.value?.toLong() ?: 0
                        jobId = matchedRegex?.groups?.get(1)?.value ?: ""
                        Log.i(TAG, "Joining place id $placeId at $jobId")
                    } else if (LogEntries.GameJoiningUniverseEntry in logEntry) {
                        val matchedRegex = LogEntries.GameJoiningUniversePattern.find(logEntry)
                        userId = matchedRegex?.groups?.get(1)?.value?.toLong() ?: 0
                        universeId = matchedRegex?.groups?.get(2)?.value?.toLong() ?: 0
                        Log.i(TAG, "Joining as $userId in universe id $universeId")
                    } else if (LogEntries.GameJoiningUDMUXEntry in logEntry) {
                        val matchedRegex = LogEntries.GameJoiningUDMUXPattern.find(logEntry)
                        udmuxIp = matchedRegex?.value ?: ""
                        Log.i(TAG, "UDMUX IP: $udmuxIp")
                    } else if (LogEntries.GameJoinedEntry in logEntry) {
                        Log.i(TAG, "Joined the server! (Setting the RPC and notificatiosn if user agreed)")
                        if (::rpcHandler.isInitialized) {
                            Log.d(TAG, "Setting RPC")
                            setGameActivityToRPC()
                        }
                        if (settingsManager.showServerLocation) {
                            Log.d(TAG, "Notifiying server location")
                            notifyServerLocation()
                        }
                    } else if (LogEntries.GameMessageEntry in logEntry) {
                        val matchedRegex = LogEntries.GameMessageEntryPattern.find(logEntry)
                        val message: BSRpcMessage = Json.decodeFromString(matchedRegex?.value ?: "{}")

                        Log.i(TAG, "Received BloxstrapRPC: $message")

                        if (::rpcHandler.isInitialized) {
                            if ((lastRPCRequestTime != 0.toLong()) && (System.currentTimeMillis() - lastRPCRequestTime) >= 1) {
                                Log.i(TAG, "Dropping RPC Message as rate limit has been reached")
                            } else {
                                handleBloxstrapRPC(message)
                            }
                            lastRPCRequestTime = System.currentTimeMillis()
                        }
                    } else if (LogEntries.GameDisconnectedEntry in logEntry) {
                        Log.i(TAG, "Leaving the game.")

                        playSessionsManager.addPlaySession(PlaySession(
                            universeId,
                            playedAt,
                            System.currentTimeMillis(),
                            "roblox://experiences/start?placeId=$placeId&gameInstanceId=$jobId"
                        ))
                        placeId = 0
                        universeId = 0
                        jobId = ""
                        udmuxIp = ""
                        userId = 0
                        playedAt = 0
                        if (::rpcHandler.isInitialized) {
                            rpcHandler.setActivity(
                                Activity(
                                    name = "Roblox",
                                    timestamps = Timestamps(
                                        start = System.currentTimeMillis()
                                    ),
                                    applicationId = DROIDBLOX_APPLICATION_ID
                                ),
                                since = 0,
                                status = "online"
                            )
                        }
                    }


                } catch (e: Exception) {
                    Log.e(TAG, "Something went wrong!; ${e.message}\nLog Entry: $logEntry")
                }
            }
        }
        Log.d(TAG, "Activity watcher ended!")
        if (placeId != 0.toLong()) {
            Log.d(TAG, "Attempting to log play session")
            playSessionsManager.addPlaySession(PlaySession(
                universeId,
                playedAt,
                System.currentTimeMillis(),
                "roblox://experiences/start?placeId=$placeId&gameInstanceId=$jobId"
            ))
        }
    }
    private suspend fun setGameActivityToRPC() {
        val thumbnailUrls = RobloxApi.fetchThumbnails(httpClient, buildList {
            add(RobloxThumbnail(
                universeId,
                "GameIcon",
                "512x512",
                false
            ))
            if (settingsManager.showRobloxUser) {
                add(RobloxThumbnail(
                    userId,
                    "AvatarHeadshot",
                    "75x75",
                    true
                ))
            }
        })

        if (thumbnailUrls == null) {
            Log.d(TAG, "Something went wrong with Roblox's Thumbnail API! Not doing anything")
            return
        }

        val mediaProxies = DiscordApi.fetchMPOfUrls(httpClient, settingsManager.token, thumbnailUrls)
        if (mediaProxies == null) {
            Log.d(TAG, "Something went wrong with Discord's Media Proxy API! Not doing anything")
            return
        }

        val gameInfo = RobloxApi.fetchGameInfo(httpClient, listOf(universeId))
        if (gameInfo == null) {
            Log.d(TAG, "Something went wrong with Roblox's Game API! Not doing anything")
            return
        }
        val gameName = gameInfo[0].name
        val gameCreator = gameInfo[0].creator
        Log.d(TAG, "$gameName by $gameCreator")

        var playingOn: String? = null
        if (settingsManager.showRobloxUser) {
            val userInfo = RobloxApi.fetchUserInfo(httpClient, userId)
            if (userInfo == null) {
                Log.d(TAG, "Something went wrong with Roblox's User API! Not doing anything")
                return
            }
            playingOn = "Playing on ${userInfo.displayName} (@${userInfo.name})"
            Log.d(TAG, playingOn)
        }

        val activityToSet = Activity(
            name = "Roblox",
            details = gameName,
            state = "by $gameCreator",
            timestamps = Timestamps(
                start = playedAt
            ),
            assets = Assets(
                largeImage = mediaProxies[0],
                smallImage = if (mediaProxies.count() == 2) mediaProxies[1] else null,
                largeText = gameName,
                smallText = playingOn
            ),
            buttons = buildList {
                add("See game page")
                if (settingsManager.allowActivityJoining) {
                    add("Join server page")
                }
            },
            metadata = Metadata(buildList {
                add("https://roblox.com/games/$placeId")
                if (settingsManager.allowActivityJoining) {
                    add("https://roblox.com/games/start?placeId=$placeId&gameInstanceid=$jobId")
                }
            })
        )
        Log.d(TAG, "Setting RPC")
        rpcHandler.setActivity(activityToSet, "online", 0.toLong())
        currentActivity = activityToSet
    }
    private suspend fun handleBloxstrapRPC(message: BSRpcMessage) {
        if (message.command == "SetLaunchData" && settingsManager.allowActivityJoining) {
            val activityToSet = currentActivity.copy(
                metadata = currentActivity.metadata!!.copy(
                    buttonUrls = currentActivity.metadata!!.buttonUrls!!.toMutableList().apply {
                        this[1] = this[1] + message.data
                    }.toList()
                )
            )
            Log.d(TAG, "Changing RPC")
            rpcHandler.setActivity(activityToSet)
        } else {
            val dataRequest: BSRpcSetRpcData = message.data as BSRpcSetRpcData
            var activityToSet = currentActivity.copy()

            if (dataRequest.details != null) {
                if (dataRequest.details.count() > 128) {
                    Log.e(TAG, "Details requested is 128 chars longer!")
                } else if (dataRequest.details == "<reset>") {
                    // dont do anything
                } else {
                    activityToSet = activityToSet.copy(details = dataRequest.details)
                }
            }
            if (dataRequest.state != null) {
                if (dataRequest.state.count() > 128) {
                    Log.e(TAG, "State requested is 128 chars longer!")
                } else if (dataRequest.state == "<reset>") {
                    // dont do anything
                } else {
                    activityToSet = activityToSet.copy(state = dataRequest.state)
                }
            }

            if (dataRequest.timeStart != null) {
                activityToSet = activityToSet.copy(timestamps = Timestamps(
                    start = dataRequest.timeStart * 1000
                ))
            }
            if (dataRequest.timeEnd != null) {
                activityToSet = activityToSet.copy(timestamps = Timestamps(
                    start = dataRequest.timeEnd * 1000
                ))
            }

            if (dataRequest.largeImage != null) {
                if (dataRequest.largeImage.clear == true) {
                    activityToSet = activityToSet.copy(assets = activityToSet.assets!!.copy(
                        largeImage = null,
                        largeText = null
                    )
                    )
                } else if (dataRequest.largeImage.reset == true) {
                    // do nothing
                } else {
                    activityToSet.copy(assets = activityToSet.assets!!.copy(
                        largeText = dataRequest.largeImage.hoverText
                    ))
                }
            }
            if (dataRequest.smallImage != null) {
                if (dataRequest.smallImage.clear == true) {
                    activityToSet = activityToSet.copy(assets = activityToSet.assets!!.copy(
                        largeImage = null,
                        largeText = null
                    ))
                } else if (dataRequest.smallImage.reset == true) {
                    // do nothing
                } else {
                    activityToSet.copy(assets = activityToSet.assets!!.copy(
                        largeText = dataRequest.smallImage.hoverText
                    ))
                }
            }
            if (dataRequest.largeImage != null || dataRequest.smallImage != null) {
                val thumbnailUrls = RobloxApi.fetchThumbnails(httpClient, buildList {
                    if (dataRequest.largeImage != null) {
                        add(RobloxThumbnail(
                            dataRequest.largeImage.assetId,
                            "Asset",
                            "512x512",
                            false
                        ))
                    } else {
                        add(RobloxThumbnail(
                            dataRequest.smallImage!!.assetId,
                            "Asset",
                            "75x75",
                            false
                        ))
                    }
                })
                if (thumbnailUrls == null) {
                    Log.e(TAG, "Something went wrong from Roblox's Thumbnail API! Doing nothing")
                    return
                }
                val mediaProxies = DiscordApi.fetchMPOfUrls(httpClient, settingsManager.token, thumbnailUrls)
                if (mediaProxies == null) {
                    Log.e(TAG, "Something went wrong from Discord's Media Proxy API! Doing nothing")
                    return
                }

                if (dataRequest.largeImage != null && dataRequest.smallImage != null) {
                    activityToSet = activityToSet.copy(assets = activityToSet.assets!!.copy(
                        largeImage = mediaProxies[0],
                        smallImage = mediaProxies[1]
                    ))
                } else if (dataRequest.largeImage != null) {
                    activityToSet = activityToSet.copy(assets = activityToSet.assets!!.copy(
                        largeImage = mediaProxies[0]
                    ))
                } else {
                    activityToSet = activityToSet.copy(assets = activityToSet.assets!!.copy(
                        smallImage = mediaProxies[0]
                    ))
                }
            }

            Log.d(TAG, "Setting RPC")
            rpcHandler.setActivity(activityToSet)
        }
    }
    private suspend fun notifyServerLocation() {
        val location = IpLocationApi.fetchIPLocation(httpClient, udmuxIp)
        if (location == null) {
            Log.e(TAG, "No location returned, not doing anything")
        } else {
            Log.i(TAG, "Connected at $location")
            Log.d(TAG, "Notifying..")
            notifHandler.notify("Located at $location")
        }
    }

    private fun writeWatcherScript() = File(context.filesDir, "activity_watcher.sh").writeText(activityWatcherScript)
}