@file:OptIn(ExperimentalJsExport::class)

package com.oddjobs.shared

import com.oddjobs.shared.repository.HttpOddjobRepository
import com.oddjobs.shared.repository.OddjobRepository

@JsExport
val ApplicationApi = ApplicationApiImpl()

@JsExport // this doesnt work :-( .. JS doesnt see the functions from oddjobrepo
class ApplicationApiImpl(
    private val oddjobRepository: OddjobRepository = HttpOddjobRepository()
) : OddjobRepository by oddjobRepository
