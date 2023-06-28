package io.tacsio.adaptive.mapek.adapter.database

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MonitoredMetricRepository : JpaRepository<MonitoredMetric, Long>