package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.ApplicationSymptoms
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

class Planner(
    private val knowledge: Knowledge,
    private val executorInChannel: Channel<Set<AdaptationAction>>
) {

    private val log = LoggerFactory.getLogger(Planner::class.java)
    val plannerInChannel = Channel<Set<ApplicationSymptoms>>()

    suspend fun start() {
        while (true) {
            val applicationSymptoms = plannerInChannel.receive()
            knowledge.analyzeSymptonsFrequency(applicationSymptoms)

            val adaptationPlan = planAdaptation(applicationSymptoms)
            log.debug("Adaptation Plan: {}", adaptationPlan)

            executorInChannel.send(adaptationPlan)
        }
    }

    private fun planAdaptation(symptoms: Set<ApplicationSymptoms>): Set<AdaptationAction> {
        return symptoms.stream()
            .filter { knowledge.canAdapt(it) }
            .map(::adaptationAction)
            .collect(Collectors.toSet())
    }

    private fun adaptationAction(it: ApplicationSymptoms?) = when (it) {
        ApplicationSymptoms.HIGH_RESPONSE_TIME -> {
            AdaptationAction.DISABLE_SUGGESTION_FEATURE
        }

        ApplicationSymptoms.LOW_RESPONSE_TIME -> {
            AdaptationAction.ENABLE_SUGGESTION_FEATURE
        }

        else -> AdaptationAction.NONE
    }
}