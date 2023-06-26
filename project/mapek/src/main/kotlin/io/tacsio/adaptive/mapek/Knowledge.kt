package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.AdaptationAction.DECREASE_REPLICAS
import io.tacsio.adaptive.mapek.model.ApplicationSymptom
import io.tacsio.adaptive.mapek.model.MonitoredAttributes
import io.tacsio.adaptive.mapek.model.MonitoredAttributes.*
import io.tacsio.adaptive.mapek.model.MonitoredData
import io.tacsio.adaptive.mapek.util.Shell
import org.slf4j.LoggerFactory
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class Knowledge {

    private val log = LoggerFactory.getLogger(Knowledge::class.java)

    private val threshold = 3

    var actualAdaptationState: MutableSet<AdaptationAction> = mutableSetOf(DECREASE_REPLICAS)

    private var latestMonitoredData: MonitoredData = MonitoredData(0.0, 0.0)

    private var monitoringChanges: MutableMap<MonitoredAttributes, Boolean> = mutableMapOf()

    private var latestSymptoms: MutableMap<ApplicationSymptom, Long> = mutableMapOf()

    private var latestAdaptationActions: MutableMap<AdaptationAction, Long> = mutableMapOf()

    fun verifyMonitoringChanges(monitoredData: MonitoredData) {
        monitoringChanges[RESPONSE_TIME] = latestMonitoredData.responseTime != monitoredData.responseTime
        monitoringChanges[THROUGHPUT] = latestMonitoredData.throughput != monitoredData.throughput

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

        val thresholdReached = monitoredAttributeChanged && frequency >= threshold
        val canAdapt = canAdapt(symptom.adaptationAction) //only to show or not the msg
        val thresholdMsg = if (thresholdReached && canAdapt) "[Threshold Reached]" else ""

        log.debug("Symptom frequency: [{}] => {} {}", symptom, frequency, thresholdMsg)

        return thresholdReached
    }

    fun canAdapt(adaptationAction: AdaptationAction): Boolean {
        if (adaptationAction == DECREASE_REPLICAS && Shell.numberOfReplicas() > 1) {
            return true
        }

        //steady state
        return actualAdaptationState.contains(adaptationAction).not()
    }

    fun updateAdaptationState(adaptationAction: AdaptationAction): AdaptationAction {
        actualAdaptationState.add(adaptationAction)
        actualAdaptationState.remove(AdaptationAction.valueOf(adaptationAction.oppositeAction))

        //adpatation frequency
        val frequency = latestAdaptationActions.getOrDefault(adaptationAction, 0)
        latestAdaptationActions[adaptationAction] = frequency + 1

        //reset frequency threshold for all monitored attributes related to symptom
        //prevent 'ping-pong' adaptations
        val oppositeAction = AdaptationAction.valueOf(adaptationAction.oppositeAction)
        val resetActionsSet = setOf(adaptationAction, oppositeAction)

        ApplicationSymptom.values()
            .filter { resetActionsSet.contains(it.adaptationAction) }
            .forEach(this::resetSymptomFrequency)

        return adaptationAction
    }

    private fun resetSymptomFrequency(symptom: ApplicationSymptom) {
        latestSymptoms[symptom] = 0
    }
}

