package io.tacsio.adaptive.mapek.adapter.database

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class MonitoredMetric(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val cpuUsage: Double,
    val memoryUsage: Double,
    val totalRequests: Double,
    val responseTime: Double,
    val throughput: Double,
    val timestamp: LocalDateTime
)