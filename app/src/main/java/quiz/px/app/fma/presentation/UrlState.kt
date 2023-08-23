package quiz.px.app.fma.presentation

import quiz.px.app.fma.presentation.utils.ConnectionErrorType

sealed class UrlState {
    object UrlGetting: UrlState()
    data class UrlExist(val url: String): UrlState()
    object UrlNotExist: UrlState()
    data class UrlError(val connectionErrorType: ConnectionErrorType, val message: String): UrlState()
}