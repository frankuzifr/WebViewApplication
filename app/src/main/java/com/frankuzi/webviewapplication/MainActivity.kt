package com.frankuzi.webviewapplication

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.webkit.ValueCallback
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.WindowCompat
import com.frankuzi.webviewapplication.presentation.UrlState
import com.frankuzi.webviewapplication.presentation.UrlViewModel
import com.frankuzi.webviewapplication.presentation.screens.ErrorContent
import com.frankuzi.webviewapplication.presentation.screens.GettingContent
import com.frankuzi.webviewapplication.presentation.screens.WebViewContent
import com.frankuzi.webviewapplication.quiz.presentation.PlugContent
import com.frankuzi.webviewapplication.quiz.presentation.QuizViewModel
import com.frankuzi.webviewapplication.ui.theme.WebViewApplicationTheme
import com.google.accompanist.web.AccompanistWebChromeClient
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    private var _filePathCallback: ValueCallback<Array<Uri>>? = null
    private var _cameraPhotoPath: Uri? = null

    private val _getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var results: Array<Uri>? = null
            if (result.data == null || result.data?.dataString.isNullOrEmpty()) {
                _cameraPhotoPath?.let {
                    results = arrayOf(it)
                }
            } else {
                val dataString = result.data?.dataString
                dataString?.let {
                    results = arrayOf(Uri.parse(dataString))
                }
            }
            _filePathCallback?.onReceiveValue(results)
            _filePathCallback = null
        } else {
            _filePathCallback?.onReceiveValue(null)
            _filePathCallback = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val urlViewModel: UrlViewModel by viewModels {
            UrlViewModel.factory
        }
        val quizViewModel: QuizViewModel by viewModels {
            QuizViewModel.factory
        }

        if (savedInstanceState == null)
            urlViewModel.getUrl()

        setContent {
            WebViewApplicationTheme(
                dynamicColor = false
            ) {
                val isSystemInDarkTheme = isSystemInDarkTheme()
                val color = MaterialTheme.colorScheme.primary

                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        val activity = view.context as Activity
                        activity.window.statusBarColor = color.toArgb()
                        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = !isSystemInDarkTheme
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Content(
                        urlViewModel = urlViewModel,
                        quizViewModel = quizViewModel,
                        applicationQuit = {
                            finishAffinity()
                            exitProcess(0)
                        },
                        webChromeClient = ChromeClient()
                    )
                }
            }
        }
    }

    inner class ChromeClient : AccompanistWebChromeClient() {
        override fun onShowFileChooser(
            webView: WebView?,
            filePathCallback: ValueCallback<Array<Uri>>?,
            fileChooserParams: FileChooserParams?
        ): Boolean {

            if (!permissionsIsGranted()) {
                return false
            }

            _filePathCallback?.onReceiveValue(null)
            _filePathCallback = filePathCallback

            val photoFile = createImageFile()
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            takePictureIntent.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
            takePictureIntent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION)

            _cameraPhotoPath = photoFile

            val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
            contentSelectionIntent.type = "image/*"

            val intentArray = arrayOf(takePictureIntent)
            val chooserIntent = Intent(Intent.ACTION_CHOOSER)
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
            _getContent.launch(chooserIntent)

            return true
        }

        @SuppressLint("SimpleDateFormat")
        private fun createImageFile(): Uri {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_${timeStamp}.jpg"
            val file = File(this@MainActivity.externalCacheDir, imageFileName)

            return FileProvider.getUriForFile(this@MainActivity, "${BuildConfig.APPLICATION_ID}.provider", file)
        }

        private fun permissionsIsGranted(): Boolean {
            if (Build.VERSION.SDK_INT >= 23) {
                when {
                    (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) -> {
                        return true
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                        showSettingsAlertDialog(R.string.need_storage_permission)
                        return false
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                        showSettingsAlertDialog(R.string.need_camera_permission)
                        return false
                    }
                    else -> {
                        requestPermissions(
                            arrayOf (
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ),
                            0)
                        return false
                    }
                }

            }

            return if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this@MainActivity,
                    arrayOf (
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ),
                    0)
                false
            }
        }

        private fun showSettingsAlertDialog(@StringRes textId: Int) {
            AlertDialog.Builder(this@MainActivity)
                .setMessage(textId)
                .setPositiveButton(R.string.settings) { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.fromParts("package", packageName, null)
                    startActivity(intent)
                }
                .setNegativeButton(R.string.no) { _, _ ->

                }
                .create()
                .show()
        }
    }
}

@Composable
fun Content(
    urlViewModel: UrlViewModel,
    quizViewModel: QuizViewModel,
    applicationQuit: () -> Unit,
    webChromeClient: AccompanistWebChromeClient
) {
    val urlState = urlViewModel.urlState.collectAsState()
    val quizScreenState = quizViewModel.currentScreen.collectAsState()
    val currentLevel = quizViewModel.currentQuestion.collectAsState()
    val answers = quizViewModel.answers.collectAsState()
    val bestScore = quizViewModel.bestScore.collectAsState()

    when (val state = urlState.value) {
        is UrlState.UrlExist -> {
            WebViewContent(
                urlExistsState = state,
                webViewChromeClient = webChromeClient
            )
        }
        UrlState.UrlNotExist -> {
            PlugContent(
                quizScreenState = quizScreenState,
                onPlayClick = {
                    quizViewModel.resetGame()
                    quizViewModel.openGameScreen()
                },
                onMenuPlayClick = {
                    quizViewModel.openMenuScreen()
                },
                onAnswerClick = { answer ->
                    quizViewModel.nextLevel()
                    quizViewModel.addAnswer(answer)
                    if (quizViewModel.currentQuestion.value == quizViewModel.questions.size)
                        quizViewModel.openEndGameScreen()
                },
                onSaveScore = { score ->
                    quizViewModel.saveScore(score)
                },
                onTimeEnd = {
                    quizViewModel.nextLevel()
                    quizViewModel.addAnswer(-1)
                    if (quizViewModel.currentQuestion.value == quizViewModel.questions.size)
                        quizViewModel.openEndGameScreen()
                },
                currentLevel = currentLevel,
                questions = quizViewModel.questions,
                answers = answers,
                bestScore = bestScore,
                exitApplication = applicationQuit
            )
        }
        UrlState.UrlGetting -> {
            GettingContent()
        }
        is UrlState.UrlError -> {
            ErrorContent(
                errorState = state,
                onRetryButtonClick = urlViewModel::getUrl
            )
        }
    }
}