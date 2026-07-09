package com.oddjobs.backend.repository

import com.oddjobs.backend.generated.jooq.tables.references.ODDJOB
import com.oddjobs.shared.dto.CreateOddjobRequest
import com.oddjobs.shared.repository.OddjobRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.web.bind.annotation.RequestBody

// suspend is only there for JS. Don't use these repo methods async
@Suppress("BlockingMethodInNonBlockingContext")
@Repository
class JooqOddjobRepository(
    private val dsl: DSLContext
) : OddjobRepository {
    override suspend fun createOddJob( request: CreateOddjobRequest) {
        dsl.insertInto(ODDJOB)
            .columns(ODDJOB.DISPLAY_NAME, ODDJOB.PROMPT)
            .values(request.name, request.prompt)
            .execute()
    }
}