package io.tacsio.adaptive.mapek.adapter.database

import io.tacsio.adaptive.mapek.adapter.MetricRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class PostgresMetricRepository(val monitoredMetricRepository: MonitoredMetricRepository) : MetricRepository {

    @Transactional(readOnly = true)
    override fun avgResponseTime(): Double {
        var avgResponseTime = monitoredMetricRepository.avgResponseTime()
        if (avgResponseTime == null) avgResponseTime = 0.0
        return avgResponseTime
    }

    @Transactional(readOnly = true)
    override fun throughput(): Double {
        var throughput = monitoredMetricRepository.throughput()
        if (throughput == null) throughput = 0.0
        return throughput
    }
}