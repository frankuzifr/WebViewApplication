package com.frankuzi.webviewapplication.quiz.data.repository

import com.frankuzi.webviewapplication.App
import com.frankuzi.webviewapplication.quiz.domain.model.Question
import com.frankuzi.webviewapplication.quiz.domain.repository.QuizRepository

class QuizRepositoryImpl : QuizRepository {
    private val _quizStorage = App.quizStorage

    override fun getQuestions(): List<Question> {
        return _quizStorage?.getQuestions() ?: listOf()
    }

    override fun saveBest(bestScore: Int) {
        _quizStorage?.saveBest(bestScore)
    }

    override fun getBest(): Int {
        return _quizStorage?.getBest() ?: 0
    }
}