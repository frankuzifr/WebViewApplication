package com.frankuzi.webviewapplication.data.repository

import com.frankuzi.webviewapplication.App
import com.frankuzi.webviewapplication.domain.repository.UrlRepository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class UrlRepositoryImpl: UrlRepository {
    private val _remoteFirebase: FirebaseRemoteConfig by lazy {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig
    }

    private val _urlFileStorage = App.urlFileStorage

    override fun getUrl(): String {

        _urlFileStorage?.let { urlFileStorage ->
            if (!urlFileStorage.exists())
                return@let

            return urlFileStorage.getUrl()
        }

        _remoteFirebase.fetchAndActivate()
        val url = _remoteFirebase.getString("url")

        if (url.isNotEmpty())
            _urlFileStorage?.saveUrl(url)

        return url
    }
}