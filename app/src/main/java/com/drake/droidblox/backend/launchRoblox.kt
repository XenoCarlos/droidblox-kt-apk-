package com.drake.droidblox.backend

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.util.Log
import java.lang.Exception
import androidx.core.net.toUri

private const val TAG: String = "DBLaunchRoblox"

fun launchRoblox(
    activity: Activity,
    deeplinkUrl: String = "roblox://"
) {
    Log.i(TAG, "Launching roblox with deeplink url: $deeplinkUrl")

    val launchIntent = Intent(Intent.ACTION_VIEW)
    val component = ComponentName("com.roblox.client", "com.roblox.client.ActivityProtocolLaunch")
    launchIntent.setComponent(component)
    launchIntent.setData(deeplinkUrl.toUri())
    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

    try {
        Log.d(TAG, "Starting intent")
        activity.startActivity(launchIntent)
    } catch (e: Exception) {
        Log.e(TAG, "Error while launching intent: ${e.message}")
    }
}