package quiz.px.app.fma.domain.repository

interface UrlRepository {
    suspend fun getUrl(): String
}