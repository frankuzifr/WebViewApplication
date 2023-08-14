package com.frankuzi.webviewapplication.presentation

import androidx.lifecycle.ViewModel
import com.frankuzi.webviewapplication.data.repository.UrlRepositoryImpl
import com.frankuzi.webviewapplication.domain.repository.UrlRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UrlViewModel(
): ViewModel() {
    private var _urlState = MutableStateFlow<UrlState>(UrlState.UrlGetting)
    val urlState: StateFlow<UrlState> = _urlState.asStateFlow()

    private val urlRepository: UrlRepository = UrlRepositoryImpl()

    fun getUrl() {
        _urlState.update {
            UrlState.UrlGetting
        }

        try {
            val url = urlRepository.getUrl()
            _urlState.update {
                if (url.isEmpty())
                    UrlState.UrlNotExist
                else
                    UrlState.UrlExist(url)
            }

        } catch (e: Throwable) {
            _urlState.update {
                UrlState.UrlError(e.message ?: "Error")
            }
        }
    }
}