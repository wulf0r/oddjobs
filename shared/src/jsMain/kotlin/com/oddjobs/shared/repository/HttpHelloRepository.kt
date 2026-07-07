package com.oddjobs.shared.repository

import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.serialization.ApiJson
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString

class HttpHelloRepository(
    baseUrl: String,
) : HelloRepository {
    private val normalizedBaseUrl = baseUrl.trimEnd('/')

    override suspend fun getHello(): HelloResponse {
        val endpoint = "$normalizedBaseUrl/hello"
        val response = window.fetch(endpoint).await()

        if (!response.ok) {
            error("GET $endpoint failed with HTTP ${response.status}")
        }

        return ApiJson.instance.decodeFromString(response.text().await())
    }
}
