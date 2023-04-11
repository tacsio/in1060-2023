package io.tacsio.adaptive.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/config")
@Configuration
class ApiConfig {
    var suggestionAlgorithmEnabled = true


    @PostMapping("/suggestion")
    fun switch() {
        suggestionAlgorithmEnabled = suggestionAlgorithmEnabled.not()
    }
}