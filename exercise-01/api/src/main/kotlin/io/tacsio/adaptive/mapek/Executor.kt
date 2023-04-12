package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Executor(private val knowledge: Knowledge) {

    private val log = LoggerFactory.getLogger(Executor::class.java)
    val executorInChannel = Channel<Set<AdaptationAction>>()

    suspend fun start() {
        while (true) {
            log.debug("Running executor")

            val adaptationActions = executorInChannel.receive()
            knowledge.latestAdaptationActions = adaptationActions

            adaptationActions.stream().forEach(::executeAdaptation)
        }
    }

    private fun executeAdaptation(adaptationAction: AdaptationAction) {
        log.debug("Executing {}", adaptationAction)

        when (adaptationAction) {
            AdaptationAction.DISABLE_SUGGESTION_FEATURE -> disableSuggestionFeature()
            AdaptationAction.ENABLE_SUGGESTION_FEATURE -> enableSuggestionFeature()
            else -> {}
        }
    }

    private fun disableSuggestionFeature() {
        val httpClient = HttpClient.newHttpClient()
        val httpRequest =
            HttpRequest.newBuilder(URI.create("http://localhost:8080/effectors/suggestion/off"))
                .build()

        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }

    private fun enableSuggestionFeature() {
        val httpClient = HttpClient.newHttpClient()
        val httpRequest =
            HttpRequest.newBuilder(URI.create("http://localhost:8080/effectors/suggestion/on"))
                .build()

        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }
}