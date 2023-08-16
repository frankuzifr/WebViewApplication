package com.frankuzi.webviewapplication.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.frankuzi.webviewapplication.presentation.QuizScreenState
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugContent(
    quizScreenState: State<QuizScreenState>,
    onPlayClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Plug")
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { paddingValue ->

        AnimatedVisibility(
            visible = quizScreenState.value == QuizScreenState.MenuScreen,
            enter = slideInHorizontally(initialOffsetX = {-1500}, animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {-1500}, animationSpec = tween(400))
        ) {
            MenuContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValue.calculateTopPadding()),
                onPlayClick = onPlayClick
            )
        }

        AnimatedVisibility(
            visible = quizScreenState.value == QuizScreenState.GameScreen,
            enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {1500}, animationSpec = tween(400))
        ) {
            GameContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValue.calculateTopPadding())
            )
        }
    }
}

@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            text = "Best score:",
            textAlign = TextAlign.End,
            fontSize = 18.sp
        )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    onPlayClick.invoke()
                }
            ) {
                Text(
                    text = "Play",
                    fontSize = 24.sp
                )
            }
        }
    }
}

@Composable
fun GameContent(
    modifier: Modifier = Modifier
) {
    var currentLevel by rememberSaveable {
        mutableIntStateOf(0)
    }
    Box(modifier = modifier) {
        (0..100).forEach {
            AnimatedVisibility(
                visible = currentLevel == it,
                enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)),
                exit = slideOutHorizontally(targetOffsetX = {-1500}, animationSpec = tween(400))
            ) {
                LevelContent(
                    name = it,
                    answerClick = {
                        if (currentLevel != 100)
                            currentLevel++
                    }
                )
            }
        }
    }
}

@Composable
fun LevelContent(
    name: Int,
    answerClick: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (buttons, question, timer) = createRefs()

        Timer(
            totalTimeMs = 40L * 1000L,
            inactiveBarColor = Color.DarkGray,
            activeBarColor = Color.Cyan,
            modifier = Modifier
                .constrainAs(timer) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(60.dp)
        )

        Text(
            text = "Question $name",
            fontSize = 24.sp,
            modifier = Modifier
                .constrainAs(question) {
                    top.linkTo(timer.top, 20.dp)
                    bottom.linkTo(buttons.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(buttons) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(start = 5.dp, end = 5.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        answerClick.invoke()
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }

                Button(
                    onClick = {
                        answerClick.invoke()
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        answerClick.invoke()
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }

                Button(
                    onClick = {
                        answerClick.invoke()
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {

                }
            }
        }
    }
}

@Composable
fun Timer(
    totalTimeMs: Long,
    inactiveBarColor: Color,
    activeBarColor: Color,
    modifier: Modifier = Modifier,
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by remember {
        mutableFloatStateOf(initialValue)
    }
    var currentTimeMs by remember {
        mutableLongStateOf(totalTimeMs)
    }

    LaunchedEffect(key1 = currentTimeMs) {
        if(currentTimeMs > 0) {
            delay(1000L)
            currentTimeMs -= 1000L
            value = currentTimeMs / totalTimeMs.toFloat()
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged {
                size = it
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = modifier
        ) {
            drawArc(
                color = inactiveBarColor,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = activeBarColor,
                startAngle = 0f,
                sweepAngle = 360f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )

        }
        val format = SimpleDateFormat("mm:ss")
        Text(
            text = format.format(Date(currentTimeMs)),
            fontSize = 14.sp
        )
    }
}