package com.frankuzi.webviewapplication.quiz.domain.repository

import com.frankuzi.webviewapplication.quiz.domain.model.Question

interface QuizRepository {
    fun getQuestions(): List<Question>
    fun saveBest(bestScore: Int)
    fun getBest(): Int
}