package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.ApplicationSymptom
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

class Planner(
    private val knowledge: Knowledge,
    private val executorInChannel: Channel<Set<AdaptationAction>>
) {

    private val log = LoggerFactory.getLogger(Planner::class.java)
    val plannerInChannel = Channel<Set<ApplicationSymptom>>()

    suspend fun start() {
        while (true) {
            val applicationSymptoms = plannerInChannel.receive()
            knowledge.analyzeSymptomsFrequency(applicationSymptoms)

            val adaptationPlan = planAdaptation(applicationSymptoms)
            log.debug("Adaptation Plan: {}", adaptationPlan)

            executorInChannel.send(adaptationPlan)
        }
    }

    private fun planAdaptation(symptoms: Set<ApplicationSymptom>): Set<AdaptationAction> {
        return symptoms.stream()
            .filter(knowledge::canPlanAdaptation)
            .map(ApplicationSymptom::adaptationAction)
            .filter(knowledge::canAdapt)
            .collect(Collectors.toSet())
    }
}