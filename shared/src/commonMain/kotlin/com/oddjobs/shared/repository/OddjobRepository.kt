package com.oddjobs.shared.repository

import com.oddjobs.shared.dto.CreateOddjobRequest
import com.oddjobs.shared.dto.ListOddjobResponse
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport


@OptIn(ExperimentalJsExport::class)
@JsExport
interface OddjobRepository {

    suspend fun createOddJob(request: CreateOddjobRequest)
    //suspend fun getOddjob(id: Long): Odd

    suspend fun listOddJobs() : ListOddjobResponse

}