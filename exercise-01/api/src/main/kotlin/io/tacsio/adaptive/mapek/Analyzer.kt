package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.ApplicationSymptoms
import io.tacsio.adaptive.mapek.model.MonitoredData
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Analyzer(
    private val knowledge: Knowledge,
    private val plannerInChannel: Channel<Set<ApplicationSymptoms>>,
) {

    private val log = LoggerFactory.getLogger(Analyzer::class.java)
    val analyzerInChannel = Channel<MonitoredData>()

    suspend fun start() {
        while (true) {
            log.debug("Running analyzer.")

            val monitoredData = analyzerInChannel.receive()
            log.debug("latest monitored data {}", monitoredData)

            val symptoms = analyzeMonitoredData(monitoredData)
            plannerInChannel.send(symptoms)
        }

    }

    private fun analyzeMonitoredData(latestMonitoredData: MonitoredData): Set<ApplicationSymptoms> {
        val historicalMonitoredData = knowledge.monitored

        var symptoms = HashSet<ApplicationSymptoms>()

        when {
            latestMonitoredData.responseTime > 500 -> symptoms.add(ApplicationSymptoms.HIGH_RESPONSE_TIME)
            latestMonitoredData.responseTime < 500 -> symptoms.add(ApplicationSymptoms.LOW_RESPONSE_TIME)
        }

        mergeMonitoredData(latestMonitoredData)

        return symptoms
    }

    private fun mergeMonitoredData(monitoredData: MonitoredData) {
        var storedData = knowledge.monitored

        val gcExecutions = monitoredData.gcExecutions
        val cpuUsage = (monitoredData.cpuUsage + storedData.cpuUsage) / 2
        val memoryUsage = (monitoredData.memoryUsage + storedData.memoryUsage) / 2
        val responseTime = (monitoredData.responseTime + storedData.responseTime) / 2

        val mergedMonitoredData = MonitoredData(gcExecutions, cpuUsage, memoryUsage, responseTime)
        knowledge.monitored = mergedMonitoredData
    }

    private fun percentage(initial: Double, final: Double): Double = (final - initial) / initial

}