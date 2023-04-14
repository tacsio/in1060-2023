package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.ApplicationSymptom
import io.tacsio.adaptive.mapek.model.ApplicationSymptom.*
import io.tacsio.adaptive.mapek.model.MonitoredData
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory

class Analyzer(
    private val knowledge: Knowledge,
    private val plannerInChannel: Channel<Set<ApplicationSymptom>>,
) {

    private val log = LoggerFactory.getLogger(Analyzer::class.java)
    val analyzerInChannel = Channel<MonitoredData>()

    suspend fun start() {
        while (true) {
            val monitoredData = analyzerInChannel.receive()

            val symptoms = analyzeMonitoredData(monitoredData)
            knowledge.analyzeSymptomsFrequency(symptoms)

            log.debug("Symptoms: {}", symptoms)

            plannerInChannel.send(symptoms)
        }
    }

    private fun analyzeMonitoredData(latestMonitoredData: MonitoredData): Set<ApplicationSymptom> {
        var symptoms = HashSet<ApplicationSymptom>()

        when {
            latestMonitoredData.responseTime > 500 -> symptoms.add(HIGH_RESPONSE_TIME)
            latestMonitoredData.responseTime <= 500 -> symptoms.add(LOW_RESPONSE_TIME)
        }

        when {
            latestMonitoredData.throughput > 800 -> symptoms.add(HIGH_THROUGHPUT)
            latestMonitoredData.throughput <= 100 -> symptoms.add(LOW_THROUGHPUT)
        }

        return symptoms
    }

    private fun percentage(initial: Double, final: Double): Double = (final - initial) / initial

}