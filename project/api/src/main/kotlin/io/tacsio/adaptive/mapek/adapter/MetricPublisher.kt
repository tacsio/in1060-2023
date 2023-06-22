package io.tacsio.adaptive.mapek.adapter

import io.tacsio.adaptive.mapek.model.MonitoredData

interface MetricPublisher {
    fun publish(monitoredData: MonitoredData)
}