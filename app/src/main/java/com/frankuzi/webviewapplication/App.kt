package com.frankuzi.webviewapplication

import android.app.Application
import com.frankuzi.webviewapplication.data.local.UrlFileStorage

class App: Application() {
    companion object {
        var urlFileStorage: UrlFileStorage? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        urlFileStorage = UrlFileStorage(this)
    }
}