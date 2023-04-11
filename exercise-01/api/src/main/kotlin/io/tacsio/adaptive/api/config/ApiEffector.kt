package io.tacsio.adaptive.api.config

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/effectors")
class ApiEffector(private val featureFlag: FeatureFlag) {

    @PostMapping("/suggestion/on")
    fun switchOnAlgorithm(): String {
        featureFlag.enableSuggestionAlgorithm(true)
        return "Suggestion algorithm enabled"
    }

    @PostMapping("/suggestion/off")
    fun switchOffAlgorithm(): String {
        featureFlag.enableSuggestionAlgorithm(false)
        return "Suggestion algorithm disabled"
    }
}