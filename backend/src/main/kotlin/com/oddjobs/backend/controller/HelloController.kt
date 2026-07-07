package com.oddjobs.backend.controller

import com.oddjobs.backend.service.HelloService
import com.oddjobs.shared.dto.HelloResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/hello")
class HelloController(
    private val helloService: HelloService,
) {
    @GetMapping
    suspend fun getHello(): HelloResponse = helloService.getHello()
}
