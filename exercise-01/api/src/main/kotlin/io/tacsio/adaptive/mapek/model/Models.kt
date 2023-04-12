package io.tacsio.adaptive.mapek.model

data class MonitoredData(
    val gcExecutions: Long,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val responseTime: Double,
)

enum class ApplicationSymptoms {
    LOW_HEAP, HIGH_MEMORY_USAGE, HIGH_CPU_USAGE, HIGH_RESPONSE_TIME,
    HIGH_HEAP, LOW_MEMORY_USAGE, LOW_CPU_USAGE, LOW_RESPONSE_TIME
}

enum class AdaptationAction {
    SCALE_UP_MEMORY, SCALE_UP_CPU, SCALE_OUT_APP, DISABLE_SUGGESTION_FEATURE,
    SCALE_DOWN_MEMORY, SCALE_DOWN_CPU, SCALE_DOWN_APP, ENABLE_SUGGESTION_FEATURE, NONE
}