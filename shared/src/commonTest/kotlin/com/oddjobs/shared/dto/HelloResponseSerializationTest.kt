package com.oddjobs.shared.dto

import com.oddjobs.shared.serialization.ApiJson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlin.test.Test
import kotlin.test.assertEquals

class HelloResponseSerializationTest {
    @Test
    fun `round trips hello response with shared json configuration`() {
        val dto = HelloResponse("Hello World from database")

        val json = ApiJson.instance.encodeToString(dto)
        val decoded = ApiJson.instance.decodeFromString<HelloResponse>(json)

        assertEquals(dto, decoded)
    }
}
