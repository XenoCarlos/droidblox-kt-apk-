package com.drake.droidblox.backend

import java.io.File
import android.util.Log
private const val TAG = "DBRootChecker"
private fun checkForRoot(): String? {
    for (i in listOf("/system/bin/su", "/system/xbin/su", "/system_ext/bin/su", "/sbin/su")) {
        if (File(i).exists()) {
            Log.i(TAG, "Device is rooted. Using $i")
            return i
        }
    }
    Log.i(TAG, "Device isn't rooted")
    return null
}

val suBinaryPath = checkForRoot()