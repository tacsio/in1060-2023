package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Analyzer(private val plannerChannel: Channel<Int>) {
    private val log = LoggerFactory.getLogger(Analyzer::class.java)
    val analyzerChannel = Channel<Int>()

    suspend fun start() {
        while (true) {
            log.info("analyzing")
            val fromChannel = analyzerChannel.receive()

            log.info("from monitor $fromChannel")
            plannerChannel.send(fromChannel * 2)
        }

    }
}