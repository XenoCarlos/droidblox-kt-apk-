package com.drake.robloxhooks

import android.content.Context
import android.util.Log
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.io.File
import java.nio.file.Paths

class Loader(
    private val context: Context
) {
    companion object {
        private const val TAG = "DBHookLoader"
    }
    init {
        Log.d(TAG, "Hooking")
        fixFFlagsHook()
    }

    private fun fixFFlagsHook() {
        Log.d(TAG, "Hooking fflags")
        XposedBridge.hookAllConstructors(
            File::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    if (param.args[0].equals("/data/local/tmp/ClientAppSettings.json")) {
                        Log.d(TAG, "Called fflags")
                        param.args[0] = Paths.get(context.filesDir.toString(), "exe/ClientSettings/ClientAppSettings.json").toString()
                    }
                }
            }
        )
    }
}