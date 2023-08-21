package com.frankuzi.webviewapplication.presentation.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView
import com.frankuzi.webviewapplication.presentation.UrlState
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.google.accompanist.web.rememberWebViewNavigator

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewContent(
    urlExistsState: UrlState.UrlExist,
    webViewChromeClient: AccompanistWebChromeClient
) {
    val webViewState = rememberSaveableWebViewState()
    val navigator = rememberWebViewNavigator()
    val webClient = remember {
        object : AccompanistWebViewClient() {}
    }
    val webChromeClient = remember {
        webViewChromeClient
    }

    BackHandler {

    }

    LaunchedEffect(navigator) {
        val bundle = webViewState.viewState
        if (bundle == null) {
            navigator.loadUrl(urlExistsState.url)
        }
    }

    WebView(
        state = webViewState,
        navigator = navigator,
        onCreated = { webView ->
            webView.webChromeClient = webViewChromeClient

            webView.settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                databaseEnabled = true
                allowFileAccess = true
                allowContentAccess = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportZoom(false)
            }


            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        },
        client = webClient,
        chromeClient = webChromeClient
    )
}