package io.tacsio.adaptive.api.dto

data class EnrollRequest(val studentId: Long, val subjectIds: Set<Long>)