package io.tacsio.adaptive.api.config.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Metrics {
    var measurements: List<Measurement> = mutableListOf()

    fun avgResponseTime(): Double {
        val totalTime = measurements.stream()
            .filter { it.statistic == "TOTAL_TIME" }
            .findFirst()
            .get().value * 1000

        val count = measurements.stream()
            .filter { it.statistic == "COUNT" }
            .findFirst()
            .get().value

        return totalTime / count
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Measurement {
    lateinit var statistic: String
    var value: Double = 0.0
}

data class MonitoredData(
    val gcExecutions: Long,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val responseTime: Double,
)
