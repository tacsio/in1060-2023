package io.tacsio.adaptive.api.config.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Metrics {
    var measurements: List<Measurement> = mutableListOf()

    fun avgResponseTime(): Double {
        val totalTime = measurements.stream()
            .filter { it.statistic == "TOTAL_TIME" }
            .findFirst()
            .orElse(Measurement(0.0))
            .value * 1000

        val requests = numberOfRequests()
        if (requests == 0.0) return 0.0

        return totalTime / requests
    }

    fun numberOfRequests(): Double {
        return measurements.stream()
            .filter { it.statistic == "COUNT" }
            .findFirst()
            .orElse(Measurement(0.0))
            .value
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Measurement {

    lateinit var statistic: String
    var value: Double = 0.0

    constructor()

    constructor(value: Double) {
        this.value = value
    }
}

data class MonitoredData(
    val gcExecutions: Long,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val totalRequests: Double,
    val responseTime: Double,
)
