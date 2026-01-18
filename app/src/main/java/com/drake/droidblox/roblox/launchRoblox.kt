package com.drake.droidblox.roblox

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.drake.droidblox.DBApplication
import com.drake.droidblox.logger.AndroidLogger
import com.drake.droidblox.logger.Logger
import com.drake.droidblox.sharedprefs.FastFlagsManager
import com.drake.droidblox.sharedprefs.SettingsManager
import java.io.File
import java.nio.file.Paths
//
//private const val TAG = "DBLaunchRoblox"
//val settingsManager: SettingsManager = DBApplication.instance.settingsManager
//val fflagsManager: FastFlagsManager = DBApplication.instance.fastFlagsManager
//val logger: Logger = AndroidLogger

fun launchRoblox(context: Context, deeplink: String = "") {
//    if (settingsManager.applyFFlags) {
//        logger.d(TAG, "Applying fast flags")
//        val currentFFlags = fflagsManager.rawFFlags
//        if (currentFFlags != null) {
//            val fflagDir = File(Paths.get(context.filesDir.toString(), "exe/ClientSettings").toString())
//            val fflagFile = File(fflagDir, "ClientAppSettings.json")
//            if (!fflagDir.exists()) {
//                logger.d(TAG, "Creating ClientSettings directory")
//                if (fflagDir.mkdirs()) {
//                    logger.d(TAG, "Successfully created directory, writing fflags")
//                    fflagFile.writeText(currentFFlags)
//                } else {
//                    logger.e(TAG, "Couldn't create directory")
//                }
//            }
//        }
//    }
//    val intent = Intent(context, Class.forName("com.roblox.client.ActivityProtocolLaunch"))
//    intent.setData(deeplink.toUri())
//    logger.d(TAG, "Starting activity")
//    context.startActivity(intent)
}