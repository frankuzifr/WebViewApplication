package com.frankuzi.webviewapplication.presentation

sealed class UrlState {
    object UrlGetting: UrlState()
    data class UrlExist(val url: String): UrlState()
    object UrlNotExist: UrlState()
    data class UrlError(val message: String): UrlState()
}