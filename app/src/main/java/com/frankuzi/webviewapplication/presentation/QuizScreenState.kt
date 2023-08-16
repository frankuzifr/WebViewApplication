package com.frankuzi.webviewapplication.presentation

sealed class QuizScreenState() {
    object MenuScreen: QuizScreenState()
    object GameScreen: QuizScreenState()
}
