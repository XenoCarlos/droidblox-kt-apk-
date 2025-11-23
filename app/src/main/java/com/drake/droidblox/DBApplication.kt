package com.drake.droidblox

//import android.app.ActivityManager
import android.app.Application
import android.content.Context
//import android.os.Debug
//import android.os.Handler
//import android.os.Looper
import android.util.Log
import com.drake.droidblox.backend.sharedprefs.playsession.PlaySessionsManager
import com.drake.droidblox.backend.sharedprefs.SettingsManager

class DBApplication : Application() {
    private val TAG = "DBApplication"

    val settingsManager: SettingsManager by lazy { SettingsManager(applicationContext) }
    val playSessionsManager: PlaySessionsManager by lazy { PlaySessionsManager(applicationContext) }

    companion object {
        // lets just make sure every class has access to it..
        lateinit var instance: DBApplication
            private set
    }

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