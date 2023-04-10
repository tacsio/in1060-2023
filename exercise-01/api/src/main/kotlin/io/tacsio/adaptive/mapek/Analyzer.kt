package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Analyzer(private val knowledge: Knowledge,
               private val plannerInChannel: Channel<Int>) {

    private val log = LoggerFactory.getLogger(Analyzer::class.java)
    val analyzerInChannel = Channel<Int>()

    suspend fun start() {
        while (true) {
            log.info("analyzing")
            val fromChannel = analyzerInChannel.receive()

            log.info(knowledge.data)
            knowledge.data = "change in analyzer"

            log.info("from monitor $fromChannel")
            plannerInChannel.send(fromChannel * 2)
        }

    }
}