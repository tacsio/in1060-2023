package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.AdaptationAction.ENABLE_SUGGESTION_FEATURE
import io.tacsio.adaptive.mapek.model.ApplicationSymptom
import io.tacsio.adaptive.mapek.model.MonitoredAttributes
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.*
import io.tacsio.adaptive.mapek.model.MonitoredData
import org.slf4j.LoggerFactory

class Knowledge {

    private val log = LoggerFactory.getLogger(Knowledge::class.java)

    private val retryLimit = 3

    var actualAdaptationState: MutableSet<AdaptationAction> = mutableSetOf(ENABLE_SUGGESTION_FEATURE)

    private var latestMonitoredData: MonitoredData = MonitoredData(0, 0.0, 0.0, 0.0)

    private var monitoringChanges: MutableMap<MonitoredAttributes, Boolean> = mutableMapOf()

    private var latestSymptoms: MutableMap<ApplicationSymptom, Long> = mutableMapOf()

    private var latestAdaptationActions: MutableMap<AdaptationAction, Long> = mutableMapOf()

    fun verifyMonitoringChanges(monitoredData: MonitoredData) {
        monitoringChanges[GC_EXECUTIONS] = latestMonitoredData.gcExecutions != monitoredData.gcExecutions
        monitoringChanges[CPU_USAGE] = latestMonitoredData.cpuUsage != monitoredData.cpuUsage
        monitoringChanges[MEMORY_USAGE] = latestMonitoredData.memoryUsage != monitoredData.memoryUsage
        monitoringChanges[RESPONSE_TIME] = latestMonitoredData.responseTime != monitoredData.responseTime

        latestMonitoredData = monitoredData
    }

    fun analyzeSymptomsFrequency(symptoms: Set<ApplicationSymptom>) {
        symptoms.forEach {
            val frequency = latestSymptoms.getOrDefault(it, 0)
            val monitoredAttributeChanged = monitoringChanges[it.monitoredAttribute] == true
            if (monitoredAttributeChanged) {
                latestSymptoms[it] = frequency + 1
            }
        }
    }

    fun canPlanAdaptation(symptom: ApplicationSymptom): Boolean {
        val frequency = latestSymptoms.getOrDefault(symptom, 0)
        val monitoredAttributeChanged = monitoringChanges[symptom.monitoredAttribute] == true

        val retryLimitReached = monitoredAttributeChanged && frequency >= retryLimit
        val retryLimitMsg = if (retryLimitReached) "[Retry Limit Reached]" else ""

        log.debug("Symptom frequency: [{}] => {} {}", symptom, frequency, retryLimitMsg)

        return retryLimitReached
    }

    fun analyzeAdaptationFrequency(adaptationActions: Set<AdaptationAction>) {
        adaptationActions.forEach {
            val frequency = latestAdaptationActions.getOrDefault(it, 0)
            latestAdaptationActions[it] = frequency + 1
        }
    }


    fun canAdapt(adaptationAction: AdaptationAction): Boolean {
        val permit = actualAdaptationState.contains(adaptationAction).not()

        if (permit) {
            actualAdaptationState.add(adaptationAction)
            actualAdaptationState.remove(AdaptationAction.valueOf(adaptationAction.oppositeAction))
        }

        return permit
    }

    fun resetSymptomFrequency(symptom: ApplicationSymptom) {
        latestSymptoms[symptom] = 0
    }
}

