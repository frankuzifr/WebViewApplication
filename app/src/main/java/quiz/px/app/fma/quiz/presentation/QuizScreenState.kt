package quiz.px.app.fma.quiz.presentation

sealed class QuizScreenState {
    object MenuScreen: QuizScreenState()
    object GameScreen: QuizScreenState()
    object EndScreen: QuizScreenState()
}
