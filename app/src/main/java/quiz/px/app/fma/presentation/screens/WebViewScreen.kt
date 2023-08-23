package quiz.px.app.fma.presentation.screens

import android.annotation.SuppressLint
import android.webkit.CookieManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import quiz.px.app.fma.presentation.UrlState

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
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, bottom = 24.dp),
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