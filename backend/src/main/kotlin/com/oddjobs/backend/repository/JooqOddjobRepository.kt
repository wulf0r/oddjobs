package com.oddjobs.backend.repository

import com.oddjobs.backend.generated.jooq.tables.references.ODDJOB
import com.oddjobs.shared.dto.SaveOddjobRequest
import com.oddjobs.shared.dto.ListOddjobResponse
import com.oddjobs.shared.dto.OddjobListItem
import com.oddjobs.shared.repository.OddjobRepository
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

// suspend is only there for JS. Don't use these repo methods async
@Suppress("BlockingMethodInNonBlockingContext")
@Repository
class JooqOddjobRepository(
    private val dsl: DSLContext
) : OddjobRepository {
    override suspend fun createOddJob( request: SaveOddjobRequest) {
        dsl.insertInto(ODDJOB)
            .columns(ODDJOB.DISPLAY_NAME, ODDJOB.PROMPT)
            .values(request.name, request.prompt)
            .execute()
    }

    override suspend fun listOddJobs(): ListOddjobResponse {
        dsl.select(ODDJOB.DISPLAY_NAME.`as`("name"), ODDJOB.PROMPT).from(ODDJOB).fetchInto(OddjobListItem::class.java).let {
            return ListOddjobResponse(items = it.toTypedArray())
        }
    }
}