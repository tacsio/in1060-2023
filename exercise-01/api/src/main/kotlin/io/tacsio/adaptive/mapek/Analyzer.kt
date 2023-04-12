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
            val monitoredData = analyzerInChannel.receive()

            val symptoms = analyzeMonitoredData(monitoredData)
            log.debug("Symptoms: {}", symptoms)

            plannerInChannel.send(symptoms)
        }

    }

    private fun analyzeMonitoredData(latestMonitoredData: MonitoredData): Set<ApplicationSymptoms> {
        var symptoms = HashSet<ApplicationSymptoms>()

        when {
            latestMonitoredData.responseTime > 500 -> symptoms.add(ApplicationSymptoms.HIGH_RESPONSE_TIME)
            latestMonitoredData.responseTime <= 500 -> symptoms.add(ApplicationSymptoms.LOW_RESPONSE_TIME)
        }

        return symptoms
    }

    private fun percentage(initial: Double, final: Double): Double = (final - initial) / initial

}