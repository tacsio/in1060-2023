package io.tacsio.adaptive.api.config

import org.springframework.context.annotation.Configuration

@Configuration
class FeatureFlag {
    var suggestionAlgorithmEnabled = true

    fun enableSuggestionAlgorithm(onOff: Boolean) {
        suggestionAlgorithmEnabled = onOff
    }
}