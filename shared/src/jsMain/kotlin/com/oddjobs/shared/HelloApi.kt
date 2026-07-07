@file:OptIn(ExperimentalJsExport::class)

package com.oddjobs.shared

import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.repository.HttpHelloRepository
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
class HelloApi(
    baseUrl: String,
) {
    private val repository = HttpHelloRepository(baseUrl)

    suspend fun loadHello(): HelloResponse = repository.getHello()
}
