package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.AdaptationAction.*
import io.tacsio.adaptive.mapek.util.Shell
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
            val adaptationActions = executorInChannel.receive()

            adaptationActions.stream()
                .map { knowledge.updateAdaptationState(it) }
                .forEach(::executeAdaptation)
        }
    }

    private fun executeAdaptation(adaptationAction: AdaptationAction) {
        log.error("Executing {}", adaptationAction)

        when (adaptationAction) {
            DISABLE_SUGGESTION_FEATURE -> disableSuggestionFeature()
            ENABLE_SUGGESTION_FEATURE -> enableSuggestionFeature()
            INCREASE_REPLICAS -> increaseReplicas()
            DECREASE_REPLICAS -> decreaseReplicas()
        }
    }

    private fun disableSuggestionFeature() {

        val httpClient = HttpClient.newHttpClient()
        val httpRequest =
            HttpRequest.newBuilder(URI.create("http://localhost:8080/effectors/suggestion/off"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build()

        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }

    private fun enableSuggestionFeature() {

        val httpClient = HttpClient.newHttpClient()
        val httpRequest =
            HttpRequest.newBuilder(URI.create("http://localhost:8080/effectors/suggestion/on"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build()

        httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())
    }

    private fun increaseReplicas() {
        try {
            Shell.execProcess("/app/kubectl scale deployment mapek --replicas 3")
        } catch (e: Exception) {
            log.error("Error when increasing replicas: {}", e.message)
        }
    }

    private fun decreaseReplicas() {
        try {
            Shell.execProcess("/app/kubectl scale deployment mapek --replicas 1")
        } catch (e: Exception) {
            log.error("Error when decreasing replicas: {}", e.message)
        }
    }
}