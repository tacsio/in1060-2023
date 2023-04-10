package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Planner(private val executorChannel: Channel<Int>) {
    private val log = LoggerFactory.getLogger(Planner::class.java)
    val plannerChannel = Channel<Int>()

    suspend fun start() {
        while (true) {
            log.info("planning")
            val fromChannel = plannerChannel.receive()

            log.info("from analyzer $fromChannel")
            executorChannel.send(fromChannel * 2)
        }
    }
}