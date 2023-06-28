package io.tacsio.adaptive.api.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.sun.management.OperatingSystemMXBean
import io.tacsio.adaptive.api.config.dto.Metrics
import io.tacsio.adaptive.api.config.dto.MonitoredData
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.management.ManagementFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse


@RestController
@RequestMapping("/sensors")
class ApiSensor {

    private val mapper = ObjectMapper()

    @GetMapping("/jvm")
    fun jvmMonitor(): MonitoredData {
        val memoryMXBean = ManagementFactory.getMemoryMXBean()
        val gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans()
        val osMXBean = ManagementFactory.getOperatingSystemMXBean()
        val hulkOsMXBean = osMXBean as OperatingSystemMXBean

        val gcExecutions = gcMXBeans.stream().map { it.collectionCount }.toList().sum()
        val cpuUsage = hulkOsMXBean.cpuLoad * 100
        val memoryUsage =
            (memoryMXBean.heapMemoryUsage.used.toDouble() / memoryMXBean.heapMemoryUsage.max.toDouble()) * 100

        val metrics = retrieveMetrics()
        val totalRequests = metrics.first
        val responseTime = metrics.second

        return MonitoredData(gcExecutions, cpuUsage, memoryUsage, totalRequests, responseTime)
    }

    @GetMapping("/jvm-full")
    fun jvmStats(): HashMap<Any, Any> {
        val response = HashMap<Any, Any>()

        val memoryMXBean = ManagementFactory.getMemoryMXBean()
        val gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans()
        val osMXBean = ManagementFactory.getOperatingSystemMXBean()
        val hulkOsMXBean = osMXBean as OperatingSystemMXBean

        val metrics = retrieveMetrics()
        val totalRequests = metrics.first
        val responseTime = metrics.second

        response["totalRequests"] = totalRequests
        response["responseTime"] = responseTime

        response["availableProcessors"] = osMXBean.availableProcessors

        response["cpu"] = hulkOsMXBean.cpuLoad * 100

        response["gc"] = mapOf(
            Pair("type", gcMXBeans.stream().map { it.name }),
            Pair("executions", gcMXBeans.stream().map { it.collectionCount }.toList().sum())
        )

        response["memory"] = mapOf(
            Pair("OsFreeMemory", bytesToMBString(hulkOsMXBean.freeMemorySize)),
            Pair("OsTotalMemory", bytesToMBString(hulkOsMXBean.totalMemorySize)),
            Pair(
                "JvmHeap", mapOf(
                    Pair("init", bytesToMBString(memoryMXBean.heapMemoryUsage.init)),
                    Pair("used", bytesToMBString(memoryMXBean.heapMemoryUsage.used)),
                    Pair("committed", bytesToMBString(memoryMXBean.heapMemoryUsage.committed)),
                    Pair("max", bytesToMBString(memoryMXBean.heapMemoryUsage.max)),
                )
            )
        )

        return response
    }

    private fun bytesToMBString(bytes: Long): String? {
        return (bytes / 1024 / 1024).toString() + " MB"
    }

    private fun retrieveMetrics(): Pair<Double, Double> {
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

        return Pair(totalRequests, responseTime)
    }
}