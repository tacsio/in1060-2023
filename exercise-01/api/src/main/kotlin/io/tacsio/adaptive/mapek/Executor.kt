package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.AdaptationAction.DISABLE_SUGGESTION_FEATURE
import io.tacsio.adaptive.mapek.model.AdaptationAction.ENABLE_SUGGESTION_FEATURE
import io.tacsio.adaptive.mapek.model.ApplicationSymptom
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
            knowledge.analyzeAdaptationFrequency(adaptationActions)

            adaptationActions.stream()
                .forEach(::executeAdaptation)
        }
    }

    private fun executeAdaptation(adaptationAction: AdaptationAction) {
        log.error("Executing {}", adaptationAction)

        //reset frequency threshold for all monitored attributes related to symptom
        //prevent 'ping-pong' adaptations
        val oppositeAction = AdaptationAction.valueOf(adaptationAction.oppositeAction)
        val resetActionsSet = setOf(adaptationAction, oppositeAction)

        ApplicationSymptom.values()
            .filter { resetActionsSet.contains(it.adaptationAction) }
            .forEach(knowledge::resetSymptomFrequency)

        when (adaptationAction) {
            DISABLE_SUGGESTION_FEATURE -> disableSuggestionFeature()
            ENABLE_SUGGESTION_FEATURE -> enableSuggestionFeature()
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
}