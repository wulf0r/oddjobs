package com.oddjobs.backend.config

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfig {
    @Bean("dbDispatcher")
    fun dbDispatcher(): CoroutineDispatcher = Dispatchers.IO.limitedParallelism(DB_PARALLELISM)

    companion object {
        // Keep blocking JDBC work bounded instead of spreading unbounded Dispatchers.IO usage through repositories.
        const val DB_PARALLELISM: Int = 16
    }
}
