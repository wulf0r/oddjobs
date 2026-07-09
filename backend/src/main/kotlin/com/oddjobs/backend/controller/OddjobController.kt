package com.oddjobs.backend.controller

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/oddjob")
class OddjobController {

    companion object {
        val CONTROLLER_URL = "/api/oddjob"

    }
}