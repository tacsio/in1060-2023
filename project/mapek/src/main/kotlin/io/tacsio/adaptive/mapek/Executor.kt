package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.AdaptationAction.DECREASE_REPLICAS
import io.tacsio.adaptive.mapek.model.AdaptationAction.INCREASE_REPLICAS
import io.tacsio.adaptive.mapek.util.Shell
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

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
        log.debug("Executing {}", adaptationAction)

        when (adaptationAction) {
            INCREASE_REPLICAS -> increaseReplicas()
            DECREASE_REPLICAS -> decreaseReplicas()
        }
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