package com.frankuzi.webviewapplication.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.frankuzi.webviewapplication.R
import com.frankuzi.webviewapplication.presentation.UrlState
import com.frankuzi.webviewapplication.presentation.utils.ConnectionErrorType

@Composable
fun ErrorContent(
    errorState: UrlState.UrlError,
    onRetryButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = when (errorState.connectionErrorType) {
                ConnectionErrorType.InternetFailed -> {
                    stringResource(id = R.string.internet_connection_problem)
                }
                ConnectionErrorType.FirebaseFailed -> {
                    errorState.message.ifEmpty {
                        stringResource(id = R.string.firebase_unknown_error)
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            onRetryButtonClick.invoke()
        }) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}