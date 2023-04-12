package io.tacsio.adaptive.mapek

import io.tacsio.adaptive.mapek.model.AdaptationAction
import io.tacsio.adaptive.mapek.model.ApplicationSymptoms
import io.tacsio.adaptive.mapek.model.MonitoredData

class Knowledge {
    var monitored: MonitoredData = MonitoredData(0, 0.0, 0.0, 0.0)
    var latestSymptoms: Set<ApplicationSymptoms> = HashSet()
    var latestAdaptationActions: Set<AdaptationAction> = HashSet()
}

