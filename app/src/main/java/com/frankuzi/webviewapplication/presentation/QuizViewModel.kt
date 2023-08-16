package com.frankuzi.webviewapplication.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel : ViewModel() {

    private var _currentScreen = MutableStateFlow<QuizScreenState>(QuizScreenState.MenuScreen)
    val currentScreen = _currentScreen.asStateFlow()

    fun openGame() {
        _currentScreen.update {
            QuizScreenState.GameScreen
        }
    }
}