package io.tacsio.adaptive.api.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.tacsio.adaptive.api.model.Student
import io.tacsio.adaptive.api.model.Subject
import java.util.*

data class AvailableSubjectResponse(
        val student: Student,
        val available: List<Subject>,
        @JsonInclude(JsonInclude.Include.NON_NULL) val suggestion: List<Subject>?,
)

data class EnrollmentResponse(val enrollmentProtocol: String = UUID.randomUUID().toString())

data class StudentResponse(val student: Student, val enrollments: List<Subject>)