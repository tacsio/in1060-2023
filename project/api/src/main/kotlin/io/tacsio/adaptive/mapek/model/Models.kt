package io.tacsio.adaptive.mapek.model

import io.tacsio.adaptive.mapek.model.AdaptationAction.*
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.RESPONSE_TIME
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.THROUGHPUT
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


data class MonitoredData(
    val cpuUsage: Double,
    val memoryUsage: Double,
    val totalRequests: Double,
    val responseTime: Double,
    val throughput: Double
) {
    val timestamp: LocalDateTime = LocalDateTime.now()
}

enum class Goal(val value: Double) {
    MAX_RESPONSE_TIME(500.0),
}

enum class MonitoredAttributes {
    CPU_USAGE, MEMORY_USAGE, RESPONSE_TIME, THROUGHPUT
}

enum class ApplicationSymptom(
    val monitoredAttribute: MonitoredAttributes,
    val adaptationAction: AdaptationAction
) {
    LOW_RESPONSE_TIME(RESPONSE_TIME, ENABLE_SUGGESTION_FEATURE),
    HIGH_RESPONSE_TIME(RESPONSE_TIME, DISABLE_SUGGESTION_FEATURE),
}

enum class AdaptationAction(val oppositeAction: String) {
    ENABLE_SUGGESTION_FEATURE("DISABLE_SUGGESTION_FEATURE"),
    DISABLE_SUGGESTION_FEATURE("ENABLE_SUGGESTION_FEATURE"),
}