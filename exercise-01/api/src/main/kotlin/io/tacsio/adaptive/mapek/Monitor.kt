package io.tacsio.adaptive.mapek

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class Monitor(private val analyzerChannel: Channel<Int>) {
    private val log = LoggerFactory.getLogger(Monitor::class.java)

    suspend fun start() {
        while (true) {
            log.info("monitoring")
            delay(1000)

            analyzerChannel.send(1)
        }
    }
}