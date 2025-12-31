package com.drake.droidblox.roblox.hooks.methodhooks

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Base64
import de.robv.android.xposed.XC_MethodHook

private const val ROBLOX_SIGNATURE = "MIICXzCCAcigAwIBAgIEU5syrjANBgkqhkiG9w0BAQUFADB0MQswCQYDVQQGEwIwMTELMAkGA1UECBMCQ0ExEjAQBgNVBAcTCVNhbiBNYXRlbzEbMBkGA1UEChMSUm9ibG94IENvcnBvcmF0aW9uMQ8wDQYDVQQLEwZNb2JpbGUxFjAUBgNVBAMTDU1hdHQgQ3JpdGVsbGkwHhcNMTQwNjEzMTcxOTQyWhcNMzkwNjA3MTcxOTQyWjB0MQswCQYDVQQGEwIwMTELMAkGA1UECBMCQ0ExEjAQBgNVBAcTCVNhbiBNYXRlbzEbMBkGA1UEChMSUm9ibG94IENvcnBvcmF0aW9uMQ8wDQYDVQQLEwZNb2JpbGUxFjAUBgNVBAMTDU1hdHQgQ3JpdGVsbGkwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAIgmjvaIC+LT1RFODUsHp92SDfYxefsJ4fV+feDb+ExJlKdHcdbH77j/m/OiPs3cpbgu1OuZngsQcRvMM005Y1NXuNHfGADXI1N9totOhNAO0fVmB2isVNYQ/idxG7l8gZVyZV5pIGXmrbFk2IcvbWEbSh7LA2SaiJ/r2PTAuovpAgMBAAEwDQYJKoZIhvcNAQEFBQADgYEAhQYWa11eTPIqewXWykCvyF45LjZmW2ou4NjLHQa/Yv2dswvGR0ktsF9xIqVxp9ciwZIxujeSGRaw2Fhe+XfNB3dfWqmIdRIV49J1kcG16KxjP+URbhdN0mk7h2hm4VfDX+u5Fx8LCUN1GnNeK9IAm25eo/Koz6xAs2YBW4YcWhc="
private val robloxSignature = Signature(Base64.decode(ROBLOX_SIGNATURE, Base64.DEFAULT))

class SignatureHook : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam?) {
        val flags = param?.args[1] as Int
        val packageInfo = param.result as? PackageInfo ?: return

        if (flags == PackageManager.GET_SIGNATURES) {
            packageInfo.signatures = arrayOf(robloxSignature)
            param.result = packageInfo
        }
    }
}