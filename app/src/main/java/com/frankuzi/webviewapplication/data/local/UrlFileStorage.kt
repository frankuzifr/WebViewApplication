package com.frankuzi.webviewapplication.data.local

import android.content.Context
import java.io.File

class UrlFileStorage(
    context: Context
) {
    private val _file = File(context.filesDir, "urlLink")

    fun exists(): Boolean {
        return _file.exists()
    }

    fun getUrl(): String {
        return _file.readText()
    }

    fun saveUrl(url: String) {
        _file.writeText(url)
    }
}