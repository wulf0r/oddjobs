package com.oddjobs.shared.repository

import com.oddjobs.shared.dto.HelloResponse

interface HelloRepository {
    suspend fun getHello(): HelloResponse
}
