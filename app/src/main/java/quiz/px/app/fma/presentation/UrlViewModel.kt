package quiz.px.app.fma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import quiz.px.app.fma.data.repository.UrlRepositoryImpl
import quiz.px.app.fma.domain.repository.UrlRepository
import quiz.px.app.fma.presentation.utils.ConnectionErrorType
import quiz.px.app.fma.presentation.utils.EmulatorChecker
import quiz.px.app.fma.presentation.utils.InternetConnectionChecker

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

        val exceptionHandler = CoroutineExceptionHandler { _, exception ->
            _urlState.update {
                UrlState.UrlError(ConnectionErrorType.FirebaseFailed, exception.message ?: "")
            }
        }

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val url = urlRepository.getUrl()
            _urlState.update {
                if (url.isEmpty())
                    UrlState.UrlNotExist
                else
                    UrlState.UrlExist(url)
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