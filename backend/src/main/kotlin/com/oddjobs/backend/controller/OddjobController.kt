package com.oddjobs.backend.controller

import com.oddjobs.shared.ApiUrls
import com.oddjobs.shared.dto.CreateOddjobRequest
import com.oddjobs.shared.repository.OddjobRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiUrls.OddjobController.BASE_URL)
class OddjobController(
    private val oddjobRepository: OddjobRepository
) {
    @PostMapping
    suspend fun createOddjobRequest(@RequestBody oddjobRequest: CreateOddjobRequest) {
        oddjobRepository.createOddJob(oddjobRequest)
    }
}