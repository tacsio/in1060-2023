package io.tacsio.adaptive.mapek.model

import io.tacsio.adaptive.mapek.model.MonitoredAttributes.*

data class MonitoredData(
    val gcExecutions: Long,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val responseTime: Double,
)

enum class MonitoredAttributes {
    GC_EXECUTIONS, CPU_USAGE, MEMORY_USAGE, RESPONSE_TIME
}

enum class ApplicationSymptoms(val monitoredAttribute: MonitoredAttributes) {
    LOW_HEAP(GC_EXECUTIONS),
    HIGH_HEAP(GC_EXECUTIONS),

    LOW_MEMORY_USAGE(MEMORY_USAGE),
    HIGH_MEMORY_USAGE(MEMORY_USAGE),

    LOW_CPU_USAGE(CPU_USAGE),
    HIGH_CPU_USAGE(CPU_USAGE),

    LOW_RESPONSE_TIME(RESPONSE_TIME),
    HIGH_RESPONSE_TIME(RESPONSE_TIME)
}

enum class AdaptationAction {
    SCALE_UP_MEMORY,
    SCALE_DOWN_MEMORY,

    SCALE_UP_CPU,
    SCALE_DOWN_CPU,

    SCALE_OUT_APP,
    SCALE_DOWN_APP,

    ENABLE_SUGGESTION_FEATURE,
    DISABLE_SUGGESTION_FEATURE,

    NONE
}