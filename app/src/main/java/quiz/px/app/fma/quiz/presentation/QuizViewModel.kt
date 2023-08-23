package quiz.px.app.fma.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import quiz.px.app.fma.quiz.data.repository.QuizRepositoryImpl
import quiz.px.app.fma.quiz.domain.repository.QuizRepository

class QuizViewModel(
    val quizRepository: QuizRepository
) : ViewModel() {

    private var _currentScreen = MutableStateFlow<QuizScreenState>(QuizScreenState.MenuScreen)
    val currentScreen = _currentScreen.asStateFlow()

    private var _currentQuestion = MutableStateFlow(0)
    var currentQuestion = _currentQuestion.asStateFlow()

    private var _answers = MutableStateFlow(mutableListOf<Int>())
    val answers = _answers.asStateFlow()

    private var _bestScore = MutableStateFlow(quizRepository.getBest())
    val bestScore = _bestScore.asStateFlow()

    val questions = quizRepository.getQuestions()

    fun openMenuScreen() {
        _currentScreen.update {
            QuizScreenState.MenuScreen
        }
    }

    fun openGameScreen() {
        _currentScreen.update {
            QuizScreenState.GameScreen
        }
    }

    fun openEndGameScreen() {
        _currentScreen.update {
            QuizScreenState.EndScreen
        }
    }

    fun nextLevel() {
        _currentQuestion.update {
            it + 1
        }
    }

    fun resetGame() {
        _currentQuestion.update {
            0
        }
        _answers.update {
            it.clear()
            it
        }
    }

    fun addAnswer(answer: Int) {
        _answers.update {
            it.add(answer)
            it
        }
    }

    fun saveScore(score: Int) {
        val best = quizRepository.getBest()
        if (score > best) {
            quizRepository.saveBest(score)
            _bestScore.update {
                score
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = QuizRepositoryImpl()

                QuizViewModel(
                    quizRepository = repository
                )
            }
        }
    }
}