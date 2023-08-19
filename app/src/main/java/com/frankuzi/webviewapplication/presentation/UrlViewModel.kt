package com.frankuzi.webviewapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.frankuzi.webviewapplication.data.repository.UrlRepositoryImpl
import com.frankuzi.webviewapplication.domain.repository.UrlRepository
import com.frankuzi.webviewapplication.presentation.utils.ConnectionErrorType
import com.frankuzi.webviewapplication.presentation.utils.EmulatorChecker
import com.frankuzi.webviewapplication.presentation.utils.InternetConnectionChecker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UrlViewModel(
    val urlRepository: UrlRepository
): ViewModel() {
    private var _urlState = MutableStateFlow<UrlState>(UrlState.UrlGetting)
    val urlState: StateFlow<UrlState> = _urlState.asStateFlow()

    private val _internetConnectionChecker = InternetConnectionChecker()
    private val _emulatorChecker = EmulatorChecker()

    fun getUrl() {

        if (_emulatorChecker.isEmulator()) {
            _urlState.update {
                UrlState.UrlNotExist
            }
            return
        }

        if (!_internetConnectionChecker.isOnline()) {
            _urlState.update {
                UrlState.UrlError(ConnectionErrorType.InternetFailed, "")
            }
            return
        }

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
                UrlState.UrlError(ConnectionErrorType.FirebaseFailed, e.message ?: "")
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = UrlRepositoryImpl()

                UrlViewModel(
                    urlRepository = repository
                )
            }
        }
    }
}