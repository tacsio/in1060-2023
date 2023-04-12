package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Executor(private val knowledge: Knowledge) {

    private val log = LoggerFactory.getLogger(Executor::class.java)
    val executorInChannel = Channel<Int>()

    suspend fun start() {
        while (true) {
            log.debug("Running executor")

            log.info(knowledge.data)
            knowledge.data = "change in executor"

            val fromChannel = executorInChannel.receive()
            log.info("from planner $fromChannel")
        }
    }
}