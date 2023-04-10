package io.tacsio.adaptive.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnrollmentController {

    @GetMapping("/")
    fun get(): ResponseEntity<*> {
        return ResponseEntity.ok("Hello")
    }

}