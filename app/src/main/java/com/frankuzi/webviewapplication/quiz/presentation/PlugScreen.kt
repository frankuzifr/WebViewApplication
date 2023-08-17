package com.frankuzi.webviewapplication.quiz.presentation

import android.annotation.SuppressLint
import android.app.GameState
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
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
import com.frankuzi.webviewapplication.quiz.domain.model.Question
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlugContent(
    quizScreenState: State<QuizScreenState>,
    onPlayClick: () -> Unit,
    onMenuPlayClick: () -> Unit,
    onAnswerClick: (Int) -> Unit,
    onSaveScore: (Int) -> Unit,
    onTimeEnd: () -> Unit,
    currentLevel: State<Int>,
    questions: List<Question>,
    answers: State<List<Int>>,
    bestScore: State<Int>,
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
            enter = slideInHorizontally(initialOffsetX = {-1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {-1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        ) {
            MenuContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValue.calculateTopPadding()),
                onPlayClick = onPlayClick,
                bestScore = bestScore
            )
        }

        val isGameScreen = quizScreenState.value is QuizScreenState.GameScreen
        AnimatedVisibility(
            visible = isGameScreen,
            enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {-1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        ) {
            GameContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValue.calculateTopPadding()),
                questions = questions,
                onAnswerClick = onAnswerClick,
                currentLevel = currentLevel,
                onTimeEnd = onTimeEnd
            )
        }

        val isEndScreen = quizScreenState.value is QuizScreenState.EndScreen
        AnimatedVisibility(
            visible = isEndScreen,
            enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        ) {
            EndContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValue.calculateTopPadding()),
                questions = questions,
                answers = answers,
                onMenuPlayClick = onMenuPlayClick,
                onSaveScore = onSaveScore
            )
        }
    }
}

@Composable
fun MenuContent(
    modifier: Modifier = Modifier,
    onPlayClick: () -> Unit,
    bestScore: State<Int>
) {
    Box(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            text = "Best score: ${bestScore.value}",
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

@SuppressLint("MutableCollectionMutableState")
@Composable
fun GameContent(
    modifier: Modifier = Modifier,
    currentLevel: State<Int>,
    questions: List<Question>,
    onAnswerClick: (Int) -> Unit,
    onTimeEnd: () -> Unit
) {

    Box(modifier = modifier) {
        questions.forEachIndexed { index, question ->
            AnimatedVisibility(
                visible = currentLevel.value == index,
                enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
                exit = slideOutHorizontally(targetOffsetX = {-1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
            ) {
                LevelContent(
                    question = question,
                    answerClick = { answerIndex ->
                        onAnswerClick.invoke(answerIndex)
                    },
                    onTimeEnd = onTimeEnd
                )
            }
        }
    }
}

@Composable
fun EndContent(
    modifier: Modifier = Modifier,
    questions: List<Question>,
    answers: State<List<Int>>,
    onMenuPlayClick: () -> Unit,
    onSaveScore: (Int) -> Unit
) {

    var countRight by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(key1 = answers) {
        answers.value.forEachIndexed { index, answerIndex ->
            val rightAnswerIndex = questions[index].rightAnswerIndex
            if (rightAnswerIndex == answerIndex)
                countRight++

            onSaveScore.invoke(countRight)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Count right anwers: $countRight")
        Button(onClick = {
            onMenuPlayClick.invoke()
        }) {
            Text(text = "Menu")
        }
    }
}

@Composable
fun LevelContent(
    question: Question,
    answerClick: (Int) -> Unit,
    onTimeEnd: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (buttons, questionView, timer) = createRefs()

        Timer(
            totalTimeMs = 10L * 1000L,
            inactiveBarColor = Color.DarkGray,
            activeBarColor = Color.Cyan,
            modifier = Modifier
                .constrainAs(timer) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(60.dp),
            onTimeEnd = onTimeEnd
        )

        Text(
            text = question.questionText,
            fontSize = 24.sp,
            modifier = Modifier
                .constrainAs(questionView) {
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
                        answerClick.invoke(0)
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = question.answerVariants[0])
                }

                Button(
                    onClick = {
                        answerClick.invoke(1)
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = question.answerVariants[1])
                }

            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        answerClick.invoke(2)
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = question.answerVariants[2])
                }

                Button(
                    onClick = {
                        answerClick.invoke(3)
                    },
                    shape = CutCornerShape(30.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = question.answerVariants[3])
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
    strokeWidth: Dp = 5.dp,
    onTimeEnd: () -> Unit
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var value by rememberSaveable {
        mutableFloatStateOf(initialValue)
    }
    var currentTimeMs by rememberSaveable {
        mutableLongStateOf(totalTimeMs)
    }

    LaunchedEffect(key1 = currentTimeMs) {
        if (currentTimeMs > 0) {
            delay(1000L)
            currentTimeMs -= 1000L
            value = currentTimeMs / totalTimeMs.toFloat()
        } else {
            onTimeEnd.invoke()
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