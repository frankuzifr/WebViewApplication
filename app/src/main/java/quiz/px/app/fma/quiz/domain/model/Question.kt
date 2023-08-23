package quiz.px.app.fma.quiz.domain.model

data class Question(
    val questionText: String,
    val answerVariants: List<String>,
    val rightAnswerIndex: Int
)