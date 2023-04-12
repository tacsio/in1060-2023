package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.MonitoredData
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Analyzer(
        private val knowledge: Knowledge,
        private val plannerInChannel: Channel<Int>,
) {

    private val log = LoggerFactory.getLogger(Analyzer::class.java)
    val analyzerInChannel = Channel<MonitoredData>()

    suspend fun start() {
        while (true) {
            log.debug("Running analyzer.")

            val monitoredData = analyzerInChannel.receive()
            log.info("monitored data {}", monitoredData)

            plannerInChannel.send(2)
        }

    }
}