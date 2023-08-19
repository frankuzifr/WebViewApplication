package com.frankuzi.webviewapplication.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.frankuzi.webviewapplication.R
import com.frankuzi.webviewapplication.ui.theme.Green900

@Composable
fun GettingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.custom_icon_foreground),
            contentDescription = "logo",
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.White)
        )
        CircularProgressIndicator(
            modifier = Modifier.size(110.dp),
            color = Green900
        )
    }
}