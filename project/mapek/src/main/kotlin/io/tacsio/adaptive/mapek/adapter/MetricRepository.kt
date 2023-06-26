package io.tacsio.adaptive.mapek.adapter

interface MetricRepository {

    fun avgResponseTime(): Double

    fun throughput(): Double
}