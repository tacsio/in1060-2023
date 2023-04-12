package io.tacsio.adaptive.api.config

import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/effectors")
class ApiEffector(private val featureFlag: FeatureFlag) {

    private val log = LoggerFactory.getLogger(ApiEffector::class.java)

    @PostMapping("/suggestion/on")
    fun switchOnAlgorithm(): String {
        featureFlag.enableSuggestionAlgorithm()
        log.debug("[Effector]: Suggestion enabled")

        return "Suggestion algorithm enabled"
    }

    @PostMapping("/suggestion/off")
    fun switchOffAlgorithm(): String {
        featureFlag.disableSuggestionAlgorithm()
        log.debug("[Effector]: Suggestion disabled")

        return "Suggestion algorithm disabled"
    }
}