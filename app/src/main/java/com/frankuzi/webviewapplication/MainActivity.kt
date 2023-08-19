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
import com.frankuzi.webviewapplication.quiz.presentation.QuizViewModel
import com.frankuzi.webviewapplication.presentation.UrlState
import com.frankuzi.webviewapplication.presentation.UrlViewModel
import com.frankuzi.webviewapplication.presentation.screens.ErrorContent
import com.frankuzi.webviewapplication.presentation.screens.GettingContent
import com.frankuzi.webviewapplication.quiz.presentation.PlugContent
import com.frankuzi.webviewapplication.presentation.screens.WebViewContent
import com.frankuzi.webviewapplication.ui.theme.WebViewApplicationTheme
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val urlViewModel: UrlViewModel by viewModels {
            UrlViewModel.factory
        }
        val quizViewModel: QuizViewModel by viewModels {
            QuizViewModel.factory
        }

        if (savedInstanceState == null)
            urlViewModel.getUrl()

        setContent {
            WebViewApplicationTheme(
                dynamicColor = false
            ) {
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
                    Content(
                        urlViewModel = urlViewModel,
                        quizViewModel = quizViewModel,
                        applicationQuit = {
                            finishAffinity()
                            exitProcess(0)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Content(
    urlViewModel: UrlViewModel,
    quizViewModel: QuizViewModel,
    applicationQuit: () -> Unit
) {
    val urlState = urlViewModel.urlState.collectAsState()
    val quizScreenState = quizViewModel.currentScreen.collectAsState()
    val currentLevel = quizViewModel.currentQuestion.collectAsState()
    val answers = quizViewModel.answers.collectAsState()
    val bestScore = quizViewModel.bestScore.collectAsState()

    when (val state = urlState.value) {
        is UrlState.UrlExist -> {
            WebViewContent(state)
        }
        UrlState.UrlNotExist -> {
            PlugContent(
                quizScreenState = quizScreenState,
                onPlayClick = {
                    quizViewModel.resetGame()
                    quizViewModel.openGameScreen()
                },
                onMenuPlayClick = {
                    quizViewModel.openMenuScreen()
                },
                onAnswerClick = { answer ->
                    quizViewModel.nextLevel()
                    quizViewModel.addAnswer(answer)
                    if (quizViewModel.currentQuestion.value == quizViewModel.questions.size)
                        quizViewModel.openEndGameScreen()
                },
                onSaveScore = { score ->
                    quizViewModel.saveScore(score)
                },
                onTimeEnd = {
                    quizViewModel.nextLevel()
                    quizViewModel.addAnswer(-1)
                    if (quizViewModel.currentQuestion.value == quizViewModel.questions.size)
                        quizViewModel.openEndGameScreen()
                },
                currentLevel = currentLevel,
                questions = quizViewModel.questions,
                answers = answers,
                bestScore = bestScore,
                exitApplication = applicationQuit
            )
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