package io.tacsio.adaptive.mapek.model

data class MonitoredData(
        val gcExecutions: Long,
        val cpuUsage: Double,
        val memoryUsage: Double,
        val responseTime: Double,
)

enum class ApplicationSymptoms {
    LOW_HEAP, HIGH_MEMORY_USAGE, HIGH_CPU_USAGE, HIGH_RESPONSE_TIME
}

enum class AdaptationAction {
    SCALE_UP_MEMORY, SCALE_UP_CPU, SCALE_OUT_APP, DISABLE_SUGGESTION_FEATURE
}