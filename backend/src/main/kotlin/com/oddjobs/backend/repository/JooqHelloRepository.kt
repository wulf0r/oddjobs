package com.oddjobs.backend.repository

import com.oddjobs.backend.generated.jooq.tables.references.HELLO_MESSAGE
import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.repository.HelloRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class JooqHelloRepository(
    private val dsl: DSLContext,
) : HelloRepository {
    override suspend fun getHello(): HelloResponse {
        val message = dsl
            .select(HELLO_MESSAGE.MESSAGE)
            .from(HELLO_MESSAGE)
            .orderBy(HELLO_MESSAGE.ID.asc())
            .limit(1)
            .fetchOne(HELLO_MESSAGE.MESSAGE)
            ?: error("No hello message configured")

        return HelloResponse(message = message)
    }
}
