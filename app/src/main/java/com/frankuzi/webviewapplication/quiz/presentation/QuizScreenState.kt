package com.frankuzi.webviewapplication.quiz.presentation

import com.frankuzi.webviewapplication.quiz.domain.model.Question

sealed class QuizScreenState {
    object MenuScreen: QuizScreenState()
    object GameScreen: QuizScreenState()
    object EndScreen: QuizScreenState()
}
