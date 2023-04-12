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
            log.debug("Running planner")

            val applicationSymptoms = plannerInChannel.receive()
            log.debug("symptoms: {}", applicationSymptoms)

            val adaptationPlan = planAdaptation(applicationSymptoms)
            executorInChannel.send(adaptationPlan)
        }
    }


    private fun planAdaptation(symptoms: Set<ApplicationSymptoms>): Set<AdaptationAction> {
        return symptoms.stream()
            .map {
                when (it) {
                    ApplicationSymptoms.HIGH_RESPONSE_TIME -> {
                        if (!knowledge.latestAdaptationActions.contains(AdaptationAction.DISABLE_SUGGESTION_FEATURE) ||
                            knowledge.latestAdaptationActions.contains(AdaptationAction.NONE)
                        ) {
                            AdaptationAction.DISABLE_SUGGESTION_FEATURE
                        } else {
                            AdaptationAction.NONE
                        }
                    }

                    ApplicationSymptoms.LOW_RESPONSE_TIME -> {
                        if (!knowledge.latestAdaptationActions.contains(AdaptationAction.ENABLE_SUGGESTION_FEATURE) ||
                            knowledge.latestAdaptationActions.contains(AdaptationAction.NONE)
                        ) {
                            AdaptationAction.ENABLE_SUGGESTION_FEATURE
                        } else {
                            AdaptationAction.NONE
                        }
                    }

                    else -> AdaptationAction.NONE
                }
            }.collect(Collectors.toSet())
    }
}