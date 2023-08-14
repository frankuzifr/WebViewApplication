package com.frankuzi.webviewapplication.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.frankuzi.webviewapplication.data.repository.UrlRepositoryImpl
import com.frankuzi.webviewapplication.domain.repository.UrlRepository
import com.frankuzi.webviewapplication.presentation.utils.InternetConnectionChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UrlViewModel(
): ViewModel() {
    private var _urlState = MutableStateFlow<UrlState>(UrlState.UrlGetting)
    val urlState: StateFlow<UrlState> = _urlState.asStateFlow()

    private val _urlRepository: UrlRepository = UrlRepositoryImpl()
    private val _internetConnectionChecker = InternetConnectionChecker()

    fun getUrl() {
        _urlState.update {
            UrlState.UrlGetting
        }

        Log.i("IsOnline", _internetConnectionChecker.isOnline().toString())
        if (!_internetConnectionChecker.isOnline()) {
            _urlState.update {
                UrlState.UrlError("Connection failed")
            }
            return
        }

        try {
            val url = _urlRepository.getUrl()
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