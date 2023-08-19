package com.frankuzi.webviewapplication.domain.repository

interface UrlRepository {
    suspend fun getUrl(): String
}