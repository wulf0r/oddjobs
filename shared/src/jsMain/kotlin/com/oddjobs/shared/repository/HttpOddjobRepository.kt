package com.oddjobs.shared.repository

import com.oddjobs.shared.ApiUrls
import com.oddjobs.shared.dto.CreateOddjobRequest
import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.serialization.ApiJson
import kotlinx.browser.window
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString
import org.w3c.fetch.RequestInit
@OptIn(ExperimentalJsExport::class)
@JsExport
class HttpOddjobRepository(
) : OddjobRepository {

    override suspend fun createOddJob(request: CreateOddjobRequest) {
        val endpoint = ApiUrls.OddjobController.BASE_URL
        val payload = ApiJson.instance.encodeToString(CreateOddjobRequest.serializer(), request)
        val response = window.fetch(
            endpoint,
            RequestInit(
                method = "POST",
                headers = js("""{"Content-Type": "application/json"}"""),
                body = payload
            )
        ).await()
        if (!response.ok) {
            error("POST $endpoint failed with HTTP ${response.status}")
        }

    }
}

