package com.frankuzi.webviewapplication.presentation.screens

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.frankuzi.webviewapplication.presentation.UrlState
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.google.accompanist.web.rememberWebViewNavigator

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(
    urkExistsState: UrlState.UrlExist
) {
    val webViewState = rememberSaveableWebViewState()
    val navigator = rememberWebViewNavigator()
    val webClient = remember {
        object : AccompanistWebViewClient() {}
    }

    BackHandler {

    }

    LaunchedEffect(navigator) {
        val bundle = webViewState.viewState
        if (bundle == null) {
            navigator.loadUrl(urkExistsState.url)
        }
    }

    WebView(
        state = webViewState,
        navigator = navigator,
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.useWideViewPort = true
            webView.settings.domStorageEnabled = true
            webView.settings.databaseEnabled = true
            webView.settings.allowFileAccess = true
            webView.settings.allowContentAccess = true
            webView.settings.javaScriptCanOpenWindowsAutomatically = true
            webView.settings.setSupportZoom(false)
            webView.webChromeClient = WebChromeClient()

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        },
        client = webClient
    )
}