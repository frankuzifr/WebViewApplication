package quiz.px.app.fma.quiz.data.repository

import quiz.px.app.fma.App
import quiz.px.app.fma.quiz.domain.model.Question
import quiz.px.app.fma.quiz.domain.repository.QuizRepository

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