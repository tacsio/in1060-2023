package io.tacsio.adaptive.mapek

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.management.OperatingSystemMXBean
import io.tacsio.adaptive.api.config.dto.Metrics
import io.tacsio.adaptive.mapek.model.MonitoredData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import java.lang.management.ManagementFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class Monitor(
    private val knowledge: Knowledge,
    private val analyzerInChannel: Channel<MonitoredData>,
) {

    private val log = LoggerFactory.getLogger(Monitor::class.java)
    private val mapper = ObjectMapper()
    private val monitoringFrequency = 5000L

    suspend fun start() {

        while (true) {
            delay(monitoringFrequency)

            val monitoredData = retrieveData()
            println("")
            log.debug("Monitored: {}", monitoredData)
            log.debug("Actual State: {}", knowledge.actualAdaptationState)

            knowledge.verifyMonitoringChanges(monitoredData)
            analyzerInChannel.send(monitoredData)
        }
    }

    private fun retrieveData(): MonitoredData {
        val memoryMXBean = ManagementFactory.getMemoryMXBean()
        val osMXBean = ManagementFactory.getOperatingSystemMXBean()
        val hulkOsMXBean = osMXBean as OperatingSystemMXBean

        val cpuUsage = hulkOsMXBean.cpuLoad * 100
        val memoryUsage =
            (memoryMXBean.heapMemoryUsage.used.toDouble() / memoryMXBean.heapMemoryUsage.max.toDouble()) * 100

        val metrics = retrieveMetrics()
        val totalRequests = metrics.first
        val responseTime = metrics.second
        val throughput = metrics.third

        return MonitoredData(cpuUsage, memoryUsage, totalRequests, responseTime, throughput)
    }

    private fun retrieveMetrics(): Triple<Double, Double, Double> {
        val httpClient = HttpClient.newHttpClient()
        val httpRequest =
            HttpRequest.newBuilder(URI.create("http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/suggestion/%7BstudentId%7D"))
                .build()

        val httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        val metrics = when (httpResponse.statusCode()) {
            HttpStatus.OK.value() -> {
                val json = httpResponse.body()
                mapper.readValue(json, Metrics::class.java)
            }

            else -> Metrics()
        }

        val totalRequests = metrics.numberOfRequests()
        val responseTime = metrics.avgResponseTime()
        val throughput = knowledge.calculateThroughput(metrics.numberOfRequests())

        return Triple(totalRequests, responseTime, throughput)
    }
}