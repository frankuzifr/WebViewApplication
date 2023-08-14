package com.frankuzi.webviewapplication

import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.frankuzi.webviewapplication.data.repository.UrlRepositoryImpl
import com.frankuzi.webviewapplication.presentation.UrlState
import com.frankuzi.webviewapplication.presentation.UrlViewModel
import com.frankuzi.webviewapplication.ui.theme.WebviewapplicationTheme
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val urlViewModel: UrlViewModel by viewModels()

        if (savedInstanceState == null)
            urlViewModel.getUrl()

        setContent {
            WebviewapplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(urlViewModel)
                }
            }
        }
    }
}

@Composable
fun Content(
    urlViewModel: UrlViewModel
) {
    val urlState = urlViewModel.urlState.collectAsState()

    when (val state = urlState.value) {
        is UrlState.UrlExist -> {
            WebViewContent(state)
        }
        UrlState.UrlNotExist -> {
            PlugContent()
        }
        UrlState.UrlGetting -> {

        }
        is UrlState.UrlError -> {
            ErrorContent(state)
        }
    }
}

@Composable
fun WebViewContent(
    urkExistsState: UrlState.UrlExist
) {

    val webViewState = rememberSaveableWebViewState()
    val navigator = rememberWebViewNavigator()
    val webClient = remember {
        object : AccompanistWebViewClient() {}
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

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webView, true)
        },
        client = webClient
    )
}

@Composable
fun PlugContent() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Plug"
        )
    }
}

@Composable
fun ErrorContent(
    errorState: UrlState.UrlError
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorState.message
        )
    }
}