package quiz.px.app.fma.quiz.domain.repository

import quiz.px.app.fma.quiz.domain.model.Question

interface QuizRepository {
    fun getQuestions(): List<Question>
    fun saveBest(bestScore: Int)
    fun getBest(): Int
}