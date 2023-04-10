package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class Monitor(private val knowledge: Knowledge,
              private val analyzerInChannel: Channel<Int>) {

    private val log = LoggerFactory.getLogger(Monitor::class.java)

    suspend fun start() {
        while (true) {
            log.info("monitoring")
            delay(1000)

            log.info(knowledge.data)
            knowledge.data = "change in monitor"

            analyzerInChannel.send(1)
        }
    }
}