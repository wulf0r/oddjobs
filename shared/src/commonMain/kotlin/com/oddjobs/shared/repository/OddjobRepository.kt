package com.oddjobs.shared.repository

import com.oddjobs.shared.dto.SaveOddjobRequest
import com.oddjobs.shared.dto.ListOddjobResponse
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@OptIn(ExperimentalJsExport::class)
@JsExport
interface OddjobRepository {

    suspend fun createOddJob(request: SaveOddjobRequest)
    //suspend fun getOddjob(id: Long): Odd

    suspend fun listOddJobs() : ListOddjobResponse

}