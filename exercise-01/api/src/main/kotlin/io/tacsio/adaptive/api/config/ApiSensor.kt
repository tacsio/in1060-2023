package io.tacsio.adaptive.api.config

import com.sun.management.OperatingSystemMXBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.management.ManagementFactory


@RestController
@RequestMapping("/sensors")
class ApiSensor {

    @GetMapping("/jvm")
    fun jvmStats(): HashMap<Any, Any> {
        val response = HashMap<Any, Any>()

        val memoryMXBean = ManagementFactory.getMemoryMXBean()
        val gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans()
        val osMXBean = ManagementFactory.getOperatingSystemMXBean()
        val hulkOsMXBean = osMXBean as OperatingSystemMXBean

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
}