package io.tacsio.adaptive.mapek.model

import io.tacsio.adaptive.mapek.model.AdaptationAction.*
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.RESPONSE_TIME
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.THROUGHPUT
import java.time.LocalTime


data class MonitoredData(
    val cpuUsage: Double,
    val memoryUsage: Double,
    val totalRequests: Double,
    val responseTime: Double,
    val throughput: Double
) {
    val timestamp: LocalTime = LocalTime.now()
}

enum class MonitoredAttributes {
    CPU_USAGE, MEMORY_USAGE, RESPONSE_TIME, THROUGHPUT
}

enum class ApplicationSymptom(
    val monitoredAttribute: MonitoredAttributes,
    val adaptationAction: AdaptationAction
) {
//    LOW_MEMORY_USAGE(MEMORY_USAGE, NONE),
//    HIGH_MEMORY_USAGE(MEMORY_USAGE, NONE),
//
//    LOW_CPU_USAGE(CPU_USAGE, NONE),
//    HIGH_CPU_USAGE(CPU_USAGE, NONE),

    LOW_RESPONSE_TIME(RESPONSE_TIME, ENABLE_SUGGESTION_FEATURE),
    HIGH_RESPONSE_TIME(RESPONSE_TIME, DISABLE_SUGGESTION_FEATURE),

    LOW_THROUGHPUT(THROUGHPUT, DECREASE_REPLICAS),
    HIGH_THROUGHPUT(THROUGHPUT, INCREASE_REPLICAS)
}

enum class AdaptationAction(val oppositeAction: String) {
//    SCALE_UP_MEMORY("SCALE_DOWN_MEMORY", ),
//    SCALE_DOWN_MEMORY("SCALE_UP_MEMORY"),
//
//    SCALE_UP_CPU("SCALE_DOWN_CPU"),
//    SCALE_DOWN_CPU("SCALE_UP_CPU"),
//
    INCREASE_REPLICAS("DECREASE_REPLICAS"),
    DECREASE_REPLICAS("INCREASE_REPLICAS"),

    ENABLE_SUGGESTION_FEATURE("DISABLE_SUGGESTION_FEATURE"),
    DISABLE_SUGGESTION_FEATURE("ENABLE_SUGGESTION_FEATURE"),
}