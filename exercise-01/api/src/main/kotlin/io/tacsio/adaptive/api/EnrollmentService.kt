package io.tacsio.adaptive.api

import io.tacsio.adaptive.api.config.FeatureFlag
import io.tacsio.adaptive.api.dto.AvailableSubjectResponse
import io.tacsio.adaptive.api.model.Enrollment
import io.tacsio.adaptive.api.model.Student
import io.tacsio.adaptive.api.model.Subject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class EnrollmentService(
    private val enrollmentRepository: EnrollmentRepository,
    private val featureFlag: FeatureFlag
) {

    private val log = LoggerFactory.getLogger(EnrollmentService::class.java)

    fun enroll(student: Student, subjectIds: Set<Long>) {
        subjectIds.mapNotNull { enrollmentRepository.findSubject(it) }
                .forEach { enroll(student, it) }
    }

    private fun enroll(student: Student, subject: Subject) {

        val canEnroll = enrollmentRepository.studentEnrollments(student).none { it.subject.id == subject.id }

        if (canEnroll) {
            val enrollment = Enrollment(UUID.randomUUID(), student, subject)
            enrollmentRepository.saveEnrollment(enrollment)
        }
    }

    fun availableSubjects(student: Student): AvailableSubjectResponse {
        val availableSubjects = enrollmentRepository.subjectsNotEnrolled(student)
        var suggestion: List<Subject>? = null

        if (featureFlag.suggestionAlgorithmEnabled) {
            suggestion = expenseAlgorithm(student)
        }

        return AvailableSubjectResponse(student, availableSubjects, suggestion)
    }

    private fun expenseAlgorithm(student: Student): List<Subject> {
        val minLimit = 100L
        val maxLimit = student.name.length * 50L
        val random = (minLimit..maxLimit).random()

        runBlocking { delay(random) }

        return enrollmentRepository.subjectsNotEnrolled(student).take((1..5).random())
    }
}