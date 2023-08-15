package com.frankuzi.webviewapplication

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.frankuzi.webviewapplication.presentation.UrlState
import com.frankuzi.webviewapplication.presentation.UrlViewModel
import com.frankuzi.webviewapplication.presentation.screens.ErrorContent
import com.frankuzi.webviewapplication.presentation.screens.GettingContent
import com.frankuzi.webviewapplication.presentation.screens.PlugContent
import com.frankuzi.webviewapplication.presentation.screens.WebViewContent
import com.frankuzi.webviewapplication.ui.theme.WebViewApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val urlViewModel: UrlViewModel by viewModels {
            UrlViewModel.factory
        }

        if (savedInstanceState == null)
            urlViewModel.getUrl()

        setContent {
            WebViewApplicationTheme {
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val color = MaterialTheme.colorScheme.primary

                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val activity = view.context as Activity
                        activity.window.statusBarColor = color.toArgb()
                        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !isSystemInDarkTheme
                    }
                }

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
//            PlugContent()
            WebViewContent(state)
        }
        UrlState.UrlNotExist -> {
            PlugContent()
        }
        UrlState.UrlGetting -> {
            GettingContent()
        }
        is UrlState.UrlError -> {
            ErrorContent(
                errorState = state,
                onRetryButtonClick = urlViewModel::getUrl
            )
        }
    }
}