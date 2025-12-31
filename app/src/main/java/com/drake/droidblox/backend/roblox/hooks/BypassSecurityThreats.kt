package com.drake.droidblox.backend.roblox.hooks

import android.content.ContentResolver
import android.util.Log
import com.drake.droidblox.DBApplication
import com.drake.droidblox.backend.Logger
import com.drake.droidblox.backend.roblox.hooks.methodhooks.DevelopmentSettingsHook
import com.drake.droidblox.backend.roblox.hooks.methodhooks.ShellCommandsHook
import com.drake.droidblox.backend.roblox.hooks.methodhooks.SignatureHook
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class BypassSecurityThreats : IXposedHookLoadPackage {
    companion object {
        private const val TAG = "DBBypass"
        private val logger: Logger = DBApplication.instance.logger
    }
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        fakeSignature(lpparam)
        fakeDeveloperModeDisabled(lpparam)
        fakeShellCommands(lpparam)
    }
    private fun fakeSignature(lpparam: XC_LoadPackage.LoadPackageParam?) {
        logger.d(TAG, "Faking app signature")

        XposedHelpers.findAndHookMethod(
            "android.app.ApplicationPackageManager",
            lpparam?.classLoader,
            "getPackageInfo",
            String::class.java,
            Int::class.javaPrimitiveType,
            SignatureHook()
        )
    }
    private fun fakeDeveloperModeDisabled(lpparam: XC_LoadPackage.LoadPackageParam?) {
        logger.d(TAG, "Faking developer mode as disabled")

        XposedHelpers.findAndHookMethod(
            "android.provider.Settings\$Global",
            lpparam?.classLoader,
            "getInt",
            ContentResolver::class.java,
            String::class.java,
            DevelopmentSettingsHook()
        )
        XposedHelpers.findAndHookMethod(
            "android.provider.Settings\$Global",
            lpparam?.classLoader,
            "getInt",
            ContentResolver::class.java,
            String::class.java,
            Int::class.javaPrimitiveType,
            DevelopmentSettingsHook()
        )
    }
    private fun fakeShellCommands(lpparam: XC_LoadPackage.LoadPackageParam?) {
        logger.d(TAG, "Faking shell commands")

        XposedHelpers.findAndHookMethod(
            "java.lang.Runtime",
            lpparam?.classLoader,
            "exec",
            String::class.java,
            ShellCommandsHook()
        )
    }
}