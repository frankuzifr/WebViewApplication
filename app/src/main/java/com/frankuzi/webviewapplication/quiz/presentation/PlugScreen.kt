package com.frankuzi.webviewapplication.quiz.presentation

import android.annotation.SuppressLint
import android.content.res.Resources.Theme
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
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
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.frankuzi.webviewapplication.R
import com.frankuzi.webviewapplication.quiz.domain.model.Question
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date

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
    exitApplication: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),

    ) {
        val isMenuScreen = quizScreenState.value is QuizScreenState.MenuScreen
        val isGameScreen = quizScreenState.value is QuizScreenState.GameScreen
        val isEndScreen = quizScreenState.value is QuizScreenState.EndScreen

        var timerInProgress by remember {
            mutableStateOf(true)
        }

        var dialogIsVisible by remember {
            mutableStateOf(false)
        }

        BackHandler {
            when {
                isMenuScreen -> {
                    exitApplication.invoke()
                }
                isGameScreen -> {
                    dialogIsVisible = !dialogIsVisible
                    timerInProgress = !dialogIsVisible
                }
                isEndScreen -> {
                    onMenuPlayClick.invoke()
                }
            }
        }

        AnimatedVisibility(
            visible = isMenuScreen,
            enter = slideInHorizontally(initialOffsetX = {-1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {-1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        ) {
            MenuContent(
                modifier = Modifier
                    .fillMaxSize(),
                onPlayClick = onPlayClick,
                bestScore = bestScore
            )
        }

        AnimatedVisibility(
            visible = isGameScreen,
            enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {if (isEndScreen) -1500 else 1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        ) {
            GameContent(
                modifier = Modifier
                    .fillMaxSize(),
                questions = questions,
                onAnswerClick = onAnswerClick,
                currentLevel = currentLevel,
                onTimeEnd = onTimeEnd,
                timerInProgress = timerInProgress
            )
        }

        AnimatedVisibility(
            visible = isEndScreen,
            enter = slideInHorizontally(initialOffsetX = {1500}, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400)),
            exit = slideOutHorizontally(targetOffsetX = {1500}, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
        ) {
            EndContent(
                modifier = Modifier
                    .fillMaxSize(),
                questions = questions,
                answers = answers,
                onMenuPlayClick = onMenuPlayClick,
                onSaveScore = onSaveScore
            )
        }

        if (dialogIsVisible)
            AlertDialog(
                onDismissRequest = {
                    dialogIsVisible = false
                    timerInProgress = true
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onMenuPlayClick.invoke()
                            dialogIsVisible = false
                            timerInProgress = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.yes)
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            dialogIsVisible = false
                            timerInProgress = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(text = stringResource(id = R.string.no))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.exit_menu))
                },
                text = {
                    Text(text = stringResource(id = R.string.you_sure_want_exit))
                }
            )
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
            text = stringResource(id = R.string.best_score, bestScore.value),
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
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
            ) {
                Text(
                    modifier = Modifier
                        .padding(10.dp),
                    text = stringResource(id = R.string.play),
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
    timerInProgress: Boolean,
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
                    onTimeEnd = onTimeEnd,
                    timerInProgress = timerInProgress
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
        Text(
            text = stringResource(id = R.string.count_right_answers, countRight),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                onMenuPlayClick.invoke()
            },
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.secondary
            ),
        ) {
            Text(
                modifier = Modifier
                    .padding(10.dp),
                text = stringResource(id = R.string.menu),
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun LevelContent(
    question: Question,
    timerInProgress: Boolean,
    answerClick: (Int) -> Unit,
    onTimeEnd: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (buttons, questionView, timer) = createRefs()

        Timer(
            totalTimeMs = 15L * 1000L,
            inactiveBarColor = MaterialTheme.colorScheme.onSecondary,
            activeBarColor = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier
                .constrainAs(timer) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(60.dp),
            onTimeEnd = onTimeEnd,
            timerInProgress = timerInProgress
        )

        Text(
            text = question.questionText,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(questionView) {
                    top.linkTo(timer.bottom)
                    bottom.linkTo(buttons.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(start = 20.dp, end = 20.dp)
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
            Button(
                onClick = {
                    answerClick.invoke(0)
                },
                shape = CutCornerShape(30.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = question.answerVariants[0],
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = {
                    answerClick.invoke(1)
                },
                shape = CutCornerShape(30.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = question.answerVariants[1],
                    fontSize = 18.sp
                )
            }


            Button(
                onClick = {
                    answerClick.invoke(2)
                },
                shape = CutCornerShape(30.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = question.answerVariants[2],
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = {
                    answerClick.invoke(3)
                },
                shape = CutCornerShape(30.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = question.answerVariants[3],
                    fontSize = 18.sp
                )
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
    strokeWidth: Dp = 3.dp,
    onTimeEnd: () -> Unit,
    timerInProgress: Boolean
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

    LaunchedEffect(key1 = currentTimeMs, key2 = timerInProgress) {
        if (currentTimeMs > 0) {
            if (timerInProgress) {
                delay(1000L)
                currentTimeMs -= 1000L
                value = currentTimeMs / totalTimeMs.toFloat()
            }
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