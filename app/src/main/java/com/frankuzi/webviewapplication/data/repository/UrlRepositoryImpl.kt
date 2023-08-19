package com.frankuzi.webviewapplication.data.repository

import com.frankuzi.webviewapplication.App
import com.frankuzi.webviewapplication.domain.repository.UrlRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.delay

class UrlRepositoryImpl: UrlRepository {
    private val _remoteFirebase: FirebaseRemoteConfig by lazy {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig
    }

    private val _urlFileStorage = App.urlFileStorage

    override suspend fun getUrl(): String {

        _urlFileStorage?.let { urlFileStorage ->
            if (!urlFileStorage.exists())
                return@let

            return urlFileStorage.getUrl()
        }

        var isCompleted = false
        var url = ""
        _remoteFirebase.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    url = _remoteFirebase.getString("url")
                }

                isCompleted = true
            }

        while (!isCompleted) {
            delay(100L)
        }

        if (url.isNotEmpty())
            _urlFileStorage?.saveUrl(url)

        return url
    }
}