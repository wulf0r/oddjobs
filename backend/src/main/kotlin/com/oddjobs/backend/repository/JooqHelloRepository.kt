package com.oddjobs.backend.repository

import com.oddjobs.backend.generated.jooq.tables.references.HELLO_MESSAGE
import com.oddjobs.shared.dto.HelloResponse
import com.oddjobs.shared.repository.HelloRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository

@Repository
class JooqHelloRepository(
    private val dsl: DSLContext,
    @Qualifier("dbDispatcher") private val dbDispatcher: CoroutineDispatcher,
) : HelloRepository {
    override suspend fun getHello(): HelloResponse =
        withContext(dbDispatcher) {
            val message = dsl
                .select(HELLO_MESSAGE.MESSAGE)
                .from(HELLO_MESSAGE)
                .orderBy(HELLO_MESSAGE.ID.asc())
                .limit(1)
                .fetchOne(HELLO_MESSAGE.MESSAGE)
                ?: error("No hello message configured")

            HelloResponse(message = message)
        }
}
