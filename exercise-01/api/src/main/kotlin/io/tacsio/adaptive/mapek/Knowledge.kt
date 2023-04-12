package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.ApplicationSymptoms
import io.tacsio.adaptive.mapek.model.MonitoredAttributes
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.*
import io.tacsio.adaptive.mapek.model.MonitoredData

class Knowledge {

    private var latestMonitoredData: MonitoredData = MonitoredData(0, 0.0, 0.0, 0.0)

    private var monitoringChanges: MutableMap<MonitoredAttributes, Boolean> = mutableMapOf()

    private var latestSymptoms: MutableMap<ApplicationSymptoms, Long> = mutableMapOf()

    private var latestAdaptationActions: MutableMap<AdaptationAction, Long> = mutableMapOf()

    fun verifyMonitoringChanges(monitoredData: MonitoredData) {
        monitoringChanges[GC_EXECUTIONS] = latestMonitoredData.gcExecutions != monitoredData.gcExecutions
        monitoringChanges[CPU_USAGE] = latestMonitoredData.cpuUsage != monitoredData.cpuUsage
        monitoringChanges[MEMORY_USAGE] = latestMonitoredData.memoryUsage != monitoredData.memoryUsage
        monitoringChanges[RESPONSE_TIME] = latestMonitoredData.responseTime != monitoredData.responseTime

        latestMonitoredData = monitoredData
    }

    fun analyzeSymptonsFrequency(symptoms: Set<ApplicationSymptoms>) {
        symptoms.forEach {
            val frequecy = latestSymptoms.getOrDefault(it, 0)
            val monitoredAttributeChanged = monitoringChanges[it.monitoredAttribute] == true
            if (monitoredAttributeChanged) {
                latestSymptoms[it] = frequecy + 1
            }
        }
    }

    fun canAdapt(symptom: ApplicationSymptoms): Boolean {
        val frequency = latestSymptoms.getOrDefault(symptom, 0)
        val monitoredAttributeChanged = monitoringChanges[symptom.monitoredAttribute] == true
        println("[$symptom] $frequency --- $monitoredAttributeChanged")
        return monitoredAttributeChanged && frequency >= 3
    }

    fun analyzeAdaptationFrequency(adaptationActions: Set<AdaptationAction>) {
        adaptationActions.forEach {
            val frequency = latestAdaptationActions.getOrDefault(it, 0)
            latestAdaptationActions[it] = frequency + 1
        }
    }
}

