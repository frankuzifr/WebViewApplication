package quiz.px.app.fma.data.local

import android.content.Context
import java.io.File

class UrlProvider(
    context: Context
) {
    private val _file = File(context.filesDir, "urlLink")

    fun exists(): Boolean {
        return _file.exists()
    }

    fun getUrl(): String {
        return _file.readText()
    }

    fun saveUrl(url: String) {
        _file.writeText(url)
    }
}