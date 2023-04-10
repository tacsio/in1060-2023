package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Executor {
    private val log = LoggerFactory.getLogger(Executor::class.java)
    val executorChannel = Channel<Int>()

    suspend fun start() {
        while (true) {
            log.info("Executor")

            val fromChannel = executorChannel.receive()
            log.info("from planner $fromChannel")
        }
    }
}