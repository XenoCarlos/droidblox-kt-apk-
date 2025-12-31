package com.drake.droidblox.roblox.hooks.methodhooks

import de.robv.android.xposed.XC_MethodHook

class ShellCommandsHook : XC_MethodHook() {
    override fun beforeHookedMethod(param: MethodHookParam?) {
        val command = param?.args[0] as String
        if ("mounts" in command || "ro.debuggable" in command) {
            param.args[0] = "echo 0"
        }
    }
}