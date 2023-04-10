package io.tacsio.adaptive.api

import io.tacsio.adaptive.api.model.Enrollment
import io.tacsio.adaptive.api.model.Student
import io.tacsio.adaptive.api.model.Subject
import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import java.util.*

@Service
class EnrollmentService(private val enrollmentRepository: EnrollmentRepository) {

    fun getSubjects(student: Student): List<Subject> {
        return enrollmentRepository.getSubjects(student)
    }

    fun enroll(student: Student, subject: Subject) {
        val enrollment = Enrollment(UUID.randomUUID(), student, subject)
        return enrollmentRepository.saveEnrollment(enrollment)
    }

    suspend fun getSuggestions(student: Student): List<Subject> {
        return expenseAlgorithm(student)
    }

    fun getStudent(id: Long): Student? {
        return enrollmentRepository.findStudent(id)
    }

    private suspend fun expenseAlgorithm(student: Student): List<Subject> {
        val minLimit = 100L
        val maxLimit = student.name.length * 100 * 5L
        val random = (minLimit..maxLimit).random()

        delay(random)

        return enrollmentRepository.getSubjects(student)
    }
}