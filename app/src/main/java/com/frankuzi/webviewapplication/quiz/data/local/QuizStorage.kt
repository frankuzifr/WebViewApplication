package com.frankuzi.webviewapplication.quiz.data.local

import com.frankuzi.webviewapplication.quiz.domain.model.Question

class QuizStorage {
    private var _bestScore = 0

    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                questionText = "Кто ты?",
                answerVariants = listOf(
                    "Молодец",
                    "Долбоеб",
                    "Свой в доску",
                    "Хто я"
                ),
                rightAnswerIndex = 2
            ),
            Question(
                questionText = "Ы?",
                answerVariants = listOf(
                    "Ы",
                    "Not ы",
                    "???",
                    "Кринге"
                ),
                rightAnswerIndex = 0
            ),
        )
    }

    fun saveBest(bestScore: Int) {
        _bestScore = bestScore
    }

    fun getBest(): Int {
        return _bestScore;
    }
}