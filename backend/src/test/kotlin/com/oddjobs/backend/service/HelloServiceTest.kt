package com.oddjobs.backend.service

import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.repository.HelloRepository
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HelloServiceTest {
    @Test
    fun `adds deterministic UTC timestamp to repository message`() = runTest {
        val service = HelloService(
            repository = object : HelloRepository {
                override suspend fun getHello(): HelloResponse = HelloResponse("Hello World from database")
            },
            clock = Clock.fixed(Instant.parse("2026-07-07T12:34:56.123Z"), ZoneOffset.UTC),
        )

        assertEquals(
            HelloResponse("Hello World from database — 2026-07-07T12:34:56.123Z"),
            service.getHello(),
        )
    }
}
