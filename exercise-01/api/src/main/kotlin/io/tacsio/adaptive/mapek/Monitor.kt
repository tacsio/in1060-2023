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
    private val monitoringFrequency = 3000L

    suspend fun start() {

        while (true) {
            log.debug("Running monitor")

            delay(monitoringFrequency)

            val monitoredData = retrieveData()
            analyzerInChannel.send(monitoredData)
        }
    }

    private fun retrieveData(): MonitoredData {
        val memoryMXBean = ManagementFactory.getMemoryMXBean()
        val gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans()
        val osMXBean = ManagementFactory.getOperatingSystemMXBean()
        val hulkOsMXBean = osMXBean as OperatingSystemMXBean

        val gcExecutions = gcMXBeans.stream().map { it.collectionCount }.toList().sum()
        val cpuUsage = hulkOsMXBean.cpuLoad * 100
        val memoryUsage =
            (memoryMXBean.heapMemoryUsage.used.toDouble() / memoryMXBean.heapMemoryUsage.max.toDouble()) * 100
        val responseTime = retrieveMetrics()

        return MonitoredData(gcExecutions, cpuUsage, memoryUsage, responseTime)
    }

    private fun retrieveMetrics(): Double {
        val httpClient = HttpClient.newHttpClient()
        val httpRequest =
            HttpRequest.newBuilder(URI.create("http://localhost:8080/actuator/metrics/http.server.requests?tag=uri:/suggestion/%7BstudentId%7D"))
                .build()

        val httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())

        val responseTime = when (httpResponse.statusCode()) {
            HttpStatus.OK.value() -> {
                val json = httpResponse.body()
                val metrics = mapper.readValue(json, Metrics::class.java)
                return metrics.avgResponseTime()
            }

            else -> 0.0
        }

        return responseTime
    }
}