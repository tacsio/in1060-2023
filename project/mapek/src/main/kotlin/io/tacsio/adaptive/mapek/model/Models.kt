package io.tacsio.adaptive.mapek.model

import io.tacsio.adaptive.mapek.model.AdaptationAction.DECREASE_REPLICAS
import io.tacsio.adaptive.mapek.model.AdaptationAction.INCREASE_REPLICAS
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.THROUGHPUT
import java.time.LocalTime


data class MonitoredData(val responseTime: Double, val throughput: Double) {
    val timestamp: LocalTime = LocalTime.now()
}

enum class Goal(val value: Double) {
    LOW_THROUGHPUT(50.0),
    HIGH_THROUGHPUT(300.0)
}

enum class MonitoredAttributes {
    RESPONSE_TIME, THROUGHPUT
}

enum class ApplicationSymptom(
    val monitoredAttribute: MonitoredAttributes,
    val adaptationAction: AdaptationAction
) {
    LOW_THROUGHPUT(THROUGHPUT, DECREASE_REPLICAS),
    HIGH_THROUGHPUT(THROUGHPUT, INCREASE_REPLICAS)
}

enum class AdaptationAction(val oppositeAction: String) {
    INCREASE_REPLICAS("DECREASE_REPLICAS"),
    DECREASE_REPLICAS("INCREASE_REPLICAS")

}