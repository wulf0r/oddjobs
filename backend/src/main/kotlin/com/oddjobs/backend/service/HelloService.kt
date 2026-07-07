package com.oddjobs.backend.service

import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.repository.HelloRepository
import java.time.Clock
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class HelloService(
    private val repository: HelloRepository,
    private val clock: Clock,
) {
    suspend fun getHello(): HelloResponse {
        val base = repository.getHello()
        return base.copy(message = "${base.message} — ${Instant.now(clock)}")
    }
}
