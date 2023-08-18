package com.frankuzi.webviewapplication

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.frankuzi.webviewapplication.data.local.UrlProvider
import com.frankuzi.webviewapplication.quiz.data.local.QuizStorage

class App: Application() {
    companion object {
        var urlFileStorage: UrlProvider? = null
            private set

        var connectivityManager: ConnectivityManager? = null
            private set

        var quizStorage: QuizStorage? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        urlFileStorage = UrlProvider(this)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        quizStorage = QuizStorage(this)
    }
}