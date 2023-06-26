package io.tacsio.adaptive.mapek.adapter.database

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MonitoredMetricRepository : JpaRepository<MonitoredMetric, Long> {

    @Query(
        nativeQuery = true, value = """
        select avg(mm.response_time)
        from monitored_metric mm
        where mm.timestamp >= now() - interval '30 SECONDS'
    """
    )
    fun avgResponseTime(): Double?

    @Query(
        nativeQuery = true, value = """
        select avg(mm.throughput)
        from monitored_metric mm
        where mm.timestamp >= now() - interval '30 SECONDS'
    """
    )
    fun throughput(): Double?
}