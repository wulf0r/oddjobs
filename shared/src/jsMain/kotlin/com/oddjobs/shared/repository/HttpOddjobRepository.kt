package com.oddjobs.shared.repository

import com.oddjobs.shared.ApiUrls
import com.oddjobs.shared.dto.SaveOddjobRequest
import com.oddjobs.shared.dto.ListOddjobResponse
import com.oddjobs.shared.serialization.ApiJson
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
@OptIn(ExperimentalJsExport::class)
@JsExport
class HttpOddjobRepository(
) : OddjobRepository {

    override suspend fun createOddJob(request: SaveOddjobRequest) {
        val endpoint = ApiUrls.OddjobController.BASE_URL
        val payload = ApiJson.instance.encodeToString(SaveOddjobRequest.serializer(), request)
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

    override suspend fun listOddJobs(): ListOddjobResponse {
        val endpoint = ApiUrls.OddjobController.LIST_URL
        val response = window.fetch(endpoint).await()

        if (!response.ok) {
            error("GET $endpoint failed with HTTP ${response.status}")
        }
        return ApiJson.instance.decodeFromString(response.text().await())
    }
}

