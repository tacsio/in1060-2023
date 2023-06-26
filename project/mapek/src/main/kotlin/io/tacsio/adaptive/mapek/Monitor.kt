package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.adapter.MetricRepository
import io.tacsio.adaptive.mapek.model.MonitoredData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

class Monitor(
    private val knowledge: Knowledge,
    private val analyzerInChannel: Channel<MonitoredData>,
    private val metricRepository: MetricRepository
) {

    private val log = LoggerFactory.getLogger(Monitor::class.java)
    private val monitoringFrequency = 5000L

    suspend fun start() {

        while (true) {
            delay(monitoringFrequency)

            val monitoredData = retrieveData()
            log.debug("Monitored: {}", monitoredData)
            log.debug("Actual State: {}", knowledge.actualAdaptationState)

            knowledge.verifyMonitoringChanges(monitoredData)
            analyzerInChannel.send(monitoredData)
        }
    }

    private fun retrieveData(): MonitoredData {
        val responseTime = metricRepository.avgResponseTime()
        val throughput = metricRepository.throughput()

        return MonitoredData(responseTime, throughput)
    }
}