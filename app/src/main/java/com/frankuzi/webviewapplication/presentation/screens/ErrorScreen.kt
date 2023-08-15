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
import androidx.compose.ui.unit.dp
import com.frankuzi.webviewapplication.presentation.UrlState

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
            text = errorState.message
        )
        Spacer(modifier = Modifier.height(5.dp))
        Button(onClick = {
            onRetryButtonClick.invoke()
        }) {
            Text(text = "Retry")
        }
    }
}