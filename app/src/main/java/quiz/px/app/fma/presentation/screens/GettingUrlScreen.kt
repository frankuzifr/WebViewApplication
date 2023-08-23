package quiz.px.app.fma.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import quiz.px.app.fma.R
import quiz.px.app.fma.ui.theme.Green900

@Composable
fun GettingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_px),
            contentDescription = "logo",
            modifier = Modifier
                .clip(CircleShape)
                .size(110.dp)
        )
        CircularProgressIndicator(
            modifier = Modifier.size(110.dp),
            color = Green900
        )
    }
}