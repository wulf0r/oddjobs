package com.oddjobs.backend.controller

import com.oddjobs.shared.ApiUrls
import com.oddjobs.shared.dto.SaveOddjobRequest
import com.oddjobs.shared.repository.OddjobRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController

class OddjobController(
    private val oddjobRepository: OddjobRepository
) {
    @PostMapping(ApiUrls.OddjobController.BASE_URL)
    suspend fun createOddjobRequest(@RequestBody oddjobRequest: SaveOddjobRequest) {
        oddjobRepository.createOddJob(oddjobRequest)
    }

    @GetMapping(ApiUrls.OddjobController.LIST_URL)
    suspend fun listOddJobs() = oddjobRepository.listOddJobs()
}