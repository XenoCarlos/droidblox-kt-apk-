// The code here is similar from Kizzy
// https://github.com/dead8309/Kizzy/blob/master/feature_profile/src/main/java/com/my/kizzy/feature_profile/ui/component/DiscordLoginWebView.kt

// TODO: a lot of BAD code here, refactor after migrating to DI

package com.drake.droidblox.ui.view

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.drake.droidblox.DBApplication
import com.drake.droidblox.ui.components.BasicScreen
import com.drake.droidblox.ui.components.ExtendedButton
import com.drake.droidblox.ui.view.navigation.Routes
import kotlinx.coroutines.runBlocking

const val JS_SNIPPET =
    "javascript:(function()%7Bvar%20i%3Ddocument.createElement('iframe')%3Bdocument.body.appendChild(i)%3Balert(i.contentWindow.localStorage.token.slice(1,-1))%7D)()"
private const val MOTOROLA = "motorola"
private const val SAMSUNG_USER_AGENT =
    "Mozilla/5.0 (Linux; Android 14; SM-S921U; Build/UP1A.231005.007) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.363"

private var buttonTitle by mutableStateOf("Login to Discord")
private var buttonSubtitle by mutableStateOf("Login to Discord to show your game activity")

private var callbackOfLoginButton = {}
private var navigateToLastRoute = {}
private val settingsManager = DBApplication.instance.settingsManager
private val discordApi = DBApplication.instance.discordApi

@Composable
fun LoginToDiscordButton(
    navController: NavController?
) {
    navigateToLastRoute = { navController?.navigate(Routes.LOGIN_TO_DISCORD) }
    callbackOfLoginButton = navigateToLastRoute
    ExtendedButton(
        buttonTitle,
        buttonSubtitle
    ) { callbackOfLoginButton() }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LoginToDiscordScreen(
    navController: NavController? = null,
    lastRoute: String = Routes.INTEGRATIONS
) {
    val url = "https://discord.com/login"
    BasicScreen(
        "LoginToDiscordScreen",
        navController,
        navIcon = Icons.AutoMirrored.Filled.ArrowBack,
        navIconOnClick = { navController?.navigate(lastRoute) }
    ) {
        AndroidView(factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    @Deprecated("Deprecated in Java")
                    override fun shouldOverrideUrlLoading(
                        webView: WebView,
                        url: String,
                    ): Boolean {
                        stopLoading()
                        if (url.endsWith("/app")) {
                            loadUrl(JS_SNIPPET)
                            visibility = View.GONE
                        }
                        return false
                    }
                }
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                /*
                Motorola users are not able to sign into discord in a WebView:
                This issue is the fault of how Motorola phones (on every model) form the WebKit UA,
                which breaks Discord's UA parsing. This makes the browser unidentifiable.

                see https://github.com/dead8309/Kizzy/issues/345#issuecomment-2699729072
             */
                if (android.os.Build.MANUFACTURER.equals(MOTOROLA, ignoreCase = true)) {
                    settings.userAgentString = SAMSUNG_USER_AGENT
                }
                webChromeClient = object : WebChromeClient() {
                    override fun onJsAlert(
                        view: WebView,
                        url: String,
                        message: String, // token
                        result: JsResult,
                    ): Boolean {
                        // final stage of logging in
                        settingsManager.token = message
                        buttonTitle = "Logout of Discord"
                        putUsernameOnUIAndChangeCallback(message)
                        navController?.navigate(lastRoute)
                        visibility = View.GONE
                        return true
                    }
                }
                loadUrl(url)
            }
        })
    }
}

fun putUsernameOnUIAndChangeCallback(
    token : String
) = runBlocking {
    buttonTitle = "Logout of discord"
    callbackOfLoginButton = { runBlocking {
        settingsManager.token = null
        discordApi.logout(token)
        buttonTitle = "Login to Discord"
        buttonSubtitle = "Login to Discord to show your game activity"
        callbackOfLoginButton = navigateToLastRoute

    } }

    buttonSubtitle = "Fetching username.."
    val username = discordApi.fetchUsername(token)
    if (username == null) {
        buttonSubtitle = "Couldn't fetch username."
    } else {
        buttonSubtitle = "Logged in as $username"
    }

}
