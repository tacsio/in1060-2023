package io.tacsio.adaptive.mapek.adapter.database

import io.tacsio.adaptive.mapek.adapter.MetricPublisher
import io.tacsio.adaptive.mapek.model.MonitoredData
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostgresPublisher(val monitoredMetricRepository: MonitoredMetricRepository) : MetricPublisher {

    @Transactional
    override fun publish(monitoredData: MonitoredData) {
        val monitoredMetric = MonitoredMetric(
            null,
            monitoredData.cpuUsage,
            monitoredData.memoryUsage,
            monitoredData.totalRequests,
            monitoredData.responseTime,
            monitoredData.throughput,
            monitoredData.timestamp
        )

        monitoredMetricRepository.save(monitoredMetric)
    }
}