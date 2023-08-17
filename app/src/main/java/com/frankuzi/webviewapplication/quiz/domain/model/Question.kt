package com.frankuzi.webviewapplication.quiz.domain.model

data class Question(
    val questionText: String,
    val answerVariants: List<String>,
    val rightAnswerIndex: Int
)