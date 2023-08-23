package quiz.px.app.fma.quiz.data.local

import android.content.Context
import com.google.gson.GsonBuilder
import quiz.px.app.fma.R
import quiz.px.app.fma.quiz.domain.model.Question
import java.io.File

class QuizStorage(
    val context: Context
) {
    private val _file = File(context.filesDir, "bestScore")

    fun getQuestions(): List<Question> {
        val rawResource = context.resources.openRawResource(R.raw.questions)
        val readText = rawResource.reader().readText()

        val gson = GsonBuilder().create()
        val levels = gson.fromJson(readText, Levels::class.java)
        return levels.questions
    }

    fun saveBest(bestScore: Int) {
        _file.writeText(bestScore.toString())
    }

    fun getBest(): Int {
        if (!_file.exists())
            return 0

        val readText = _file.readText()

        return readText.toInt()
    }
}

data class Levels(
    val questions: List<Question>
)