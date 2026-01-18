package com.drake.droidblox

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.drake.droidblox.apiservice.DiscordApi
import com.drake.droidblox.apiservice.IpLocationApi
import com.drake.droidblox.apiservice.RobloxApi
import com.drake.droidblox.apiservice.httpclient.customHttpClient
import com.drake.droidblox.logger.AndroidLogger
import com.drake.droidblox.sharedprefs.FastFlagsManager
import com.drake.droidblox.sharedprefs.PlaySessionsManager
import com.drake.droidblox.sharedprefs.SettingsManager
import com.drake.robloxhooks.Loader

import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DBApplication : Application() {
    companion object {
        private const val TAG = "DBApplication"
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: DBApplication
            private set
    }

//    val settingsManager: SettingsManager by lazy { SettingsManager(applicationContext) }
//    val playSessionsManager: PlaySessionsManager by lazy { PlaySessionsManager(applicationContext) }
//    val fastFlagsManager: FastFlagsManager by lazy { FastFlagsManager(applicationContext) }

    // TODO: depend on DI
//    private val httpClient = customHttpClient(AndroidLogger)
//    val discordApi: DiscordApi by lazy { DiscordApi(AndroidLogger, httpClient) }
//    val ipLocationApi: IpLocationApi by lazy { IpLocationApi(AndroidLogger, httpClient) }
//    val robloxApi: RobloxApi by lazy { RobloxApi(AndroidLogger, httpClient) }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")

        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Log.d(TAG, "attachBaseContext()")
    }
}