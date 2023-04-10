package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Planner(private val knowledge: Knowledge,
              private val executorInChannel: Channel<Int>) {

    private val log = LoggerFactory.getLogger(Planner::class.java)
    val plannerInChannel = Channel<Int>()

    suspend fun start() {
        while (true) {
            log.info("planning")
            val fromChannel = plannerInChannel.receive()

            log.info(knowledge.data)
            knowledge.data = "change in planner"

            log.info("from analyzer $fromChannel")
            executorInChannel.send(fromChannel * 2)
        }
    }
}