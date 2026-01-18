package com.drake.droidblox.apiservice

import com.drake.droidblox.apiservice.models.roblox.RawRobloxGame
import com.drake.droidblox.apiservice.models.roblox.RawRobloxThumbnailResponse
import com.drake.droidblox.apiservice.models.roblox.RobloxGame
import com.drake.droidblox.apiservice.models.roblox.RobloxThumbnail
import com.drake.droidblox.apiservice.models.roblox.RobloxUser
import com.drake.droidblox.logger.Logger
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobloxApi @Inject constructor(
    private val logger: Logger,
    private val httpClient: HttpClient
) {
    companion object {
        private const val TAG = "RobloxApi"
    }

    suspend fun fetchGameInfo(
        universeIds: List<Long>
    ): List<RobloxGame>? {
        logger.d(TAG, "Fetching game info for the following universe id(s): $universeIds")
        val gamesInfoReq: HttpResponse = httpClient.get(
            "https://games.roblox.com/v1/games?universeIds=${universeIds.joinToString(",")}"
        )

        if (gamesInfoReq.status != HttpStatusCode.OK) {
            logger.e(TAG, "Couldn't fetch game info for the following universe id(s): $universeIds")
            return null
        } else {
            val gamesInfo: RawRobloxGame = gamesInfoReq.body()
            val listOfRobloxGame: List<RobloxGame> = universeIds.flatMap { universeId ->
                gamesInfo.data.filter { game -> game.universeId == universeId }
            } // roblox fucks up the returned data if there are duplicates of universe ids
            logger.d(TAG, "universe id(s): $universeIds data: $listOfRobloxGame")
            return listOfRobloxGame
        }

    }

    suspend fun fetchGameInfo(universeId: Long) = fetchGameInfo(listOf(universeId))

    suspend fun fetchUserInfo(
        userId: Long
    ): RobloxUser? {
        logger.d(TAG, "fetching user info for user id: $userId")
        val usernameReq: HttpResponse = httpClient.get(
            "https://users.roblox.com/v1/users/$userId"
        )
        if (usernameReq.status != HttpStatusCode.OK) {
            logger.e(TAG, "Couldn't fetch user info for user id: $userId")
           return null
        } else {
            val userInfo: RobloxUser = usernameReq.body<RobloxUser>()
            logger.d(TAG, "user id: $userId data: $userInfo")
            return userInfo
        }
    }


    suspend fun fetchThumbnailUrl(
        thumbnails: List<RobloxThumbnail>
    ): List<String>? {
        logger.d(TAG, "fetching thumbnail urls for the following thumbnails: $thumbnails")
        val thumbnailsReq: HttpResponse = httpClient.post(
            "https://thumbnails.roblox.com/v1/batch"
        ) {
            contentType(ContentType.Application.Json)
            setBody(thumbnails)
        }
        if (thumbnailsReq.status != HttpStatusCode.OK) {
            logger.e(TAG, "failed to fetch thumbnail urls for the following thumbnails: $thumbnails")
            return null
        } else {
            val thumbnailUrls: List<String> = thumbnailsReq.body<RawRobloxThumbnailResponse>().data.map {
                it.imageUrl
            }
            logger.d(TAG, "thumbnails: $thumbnails data: $thumbnailUrls")
            return thumbnailUrls
        }
    }

    suspend fun fetchThumbnailUrl(thumbnail: RobloxThumbnail) = fetchThumbnailUrl(listOf(thumbnail))
}
