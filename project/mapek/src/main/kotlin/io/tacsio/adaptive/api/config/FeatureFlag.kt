package io.tacsio.adaptive.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class FeatureFlag {

    @Value("\${api.feature.suggestion.enabled}")
    var suggestionAlgorithmEnabled = true

    fun enableSuggestionAlgorithm() {
        suggestionAlgorithmEnabled = true
    }

    fun disableSuggestionAlgorithm() {
        suggestionAlgorithmEnabled = false;
    }
}