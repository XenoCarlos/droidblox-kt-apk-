package com.drake.droidblox.backend.roblox.hooks.methodhooks

import android.provider.Settings
import de.robv.android.xposed.XC_MethodHook

class DevelopmentSettingsHook : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam?) {
        val name = param?.args[1] as String
        if (Settings.Global.DEVELOPMENT_SETTINGS_ENABLED == name) {
            param.result = 0
        }
    }
}