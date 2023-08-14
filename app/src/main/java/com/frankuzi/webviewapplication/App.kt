package com.frankuzi.webviewapplication

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.frankuzi.webviewapplication.data.local.UrlFileStorage

class App: Application() {
    companion object {
        var urlFileStorage: UrlFileStorage? = null
            private set

        var connectivityManager: ConnectivityManager? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        urlFileStorage = UrlFileStorage(this)
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
}