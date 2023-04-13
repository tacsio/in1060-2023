package io.tacsio.adaptive.mapek.model

import io.tacsio.adaptive.mapek.model.AdaptationAction.DISABLE_SUGGESTION_FEATURE
import io.tacsio.adaptive.mapek.model.AdaptationAction.ENABLE_SUGGESTION_FEATURE
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.RESPONSE_TIME

data class MonitoredData(
    val gcExecutions: Long,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val responseTime: Double,
)

enum class MonitoredAttributes {
    GC_EXECUTIONS, CPU_USAGE, MEMORY_USAGE, RESPONSE_TIME
}

enum class ApplicationSymptom(
    val monitoredAttribute: MonitoredAttributes,
    val adaptationAction: AdaptationAction
) {
//    LOW_HEAP(GC_EXECUTIONS, NONE),
//    HIGH_HEAP(GC_EXECUTIONS, NONE),
//
//    LOW_MEMORY_USAGE(MEMORY_USAGE, NONE),
//    HIGH_MEMORY_USAGE(MEMORY_USAGE, NONE),
//
//    LOW_CPU_USAGE(CPU_USAGE, NONE),
//    HIGH_CPU_USAGE(CPU_USAGE, NONE),

    LOW_RESPONSE_TIME(RESPONSE_TIME, ENABLE_SUGGESTION_FEATURE),
    HIGH_RESPONSE_TIME(RESPONSE_TIME, DISABLE_SUGGESTION_FEATURE),
}

enum class AdaptationAction(val oppositeAction: String) {
//    SCALE_UP_MEMORY("SCALE_DOWN_MEMORY", ),
//    SCALE_DOWN_MEMORY("SCALE_UP_MEMORY"),
//
//    SCALE_UP_CPU("SCALE_DOWN_CPU"),
//    SCALE_DOWN_CPU("SCALE_UP_CPU"),
//
//    SCALE_OUT_APP("SCALE_DOWN_APP"),
//    SCALE_DOWN_APP("SCALE_OUT_APP"),

    ENABLE_SUGGESTION_FEATURE("DISABLE_SUGGESTION_FEATURE"),
    DISABLE_SUGGESTION_FEATURE("ENABLE_SUGGESTION_FEATURE"),
}