package io.tacsio.adaptive.api

import io.tacsio.adaptive.api.dto.AvailableSubjectResponse
import io.tacsio.adaptive.api.dto.EnrollRequest
import io.tacsio.adaptive.api.dto.EnrollmentResponse
import io.tacsio.adaptive.api.dto.StudentResponse
import io.tacsio.adaptive.api.model.Enrollment
import io.tacsio.adaptive.api.model.Student
import io.tacsio.adaptive.api.model.Subject
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class EnrollmentController(
        private val enrollmentService: EnrollmentService,
        private val enrollmentRepository: EnrollmentRepository,
) {

    @GetMapping("/")
    fun get(): ResponseEntity<*> {
        return ResponseEntity.ok("https://github.com/tacsio/in1060-2023/tree/main/exercise-01/api")
    }


    @GetMapping("/suggestion/{studentId}")
    fun suggestions(@PathVariable studentId: Long): ResponseEntity<AvailableSubjectResponse> {

        val student = enrollmentRepository.findStudent(studentId)

        val available = student
                ?.let { runBlocking { enrollmentService.availableSubjects(it) } }
                ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(available)
    }

    @PostMapping("/enroll")
    fun enroll(@RequestBody enrollRequest: EnrollRequest): ResponseEntity<EnrollmentResponse> {
        val student = enrollmentRepository.findStudent(enrollRequest.studentId)

       student
                ?.let { enrollmentService.enroll(student, enrollRequest.subjectIds) }
                ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(EnrollmentResponse())
    }


    //////////////////////////////////////////////////////////////////////////////

    @GetMapping("/students")
    fun students(): ResponseEntity<List<Student>> {
        val students = enrollmentRepository.students()

        return ResponseEntity.ok(students)
    }

    @GetMapping("/students/{id}")
    fun student(@PathVariable id: Long): ResponseEntity<StudentResponse> {
        val student = enrollmentRepository.findStudent(id)

        val enrollments = student
                ?.let { enrollmentRepository.studentEnrollments(student) }
                ?: return ResponseEntity.notFound().build()

        val enrollmentResponse = StudentResponse(student, enrollments.map { it.subject })

        return ResponseEntity.ok(enrollmentResponse)
    }

    @GetMapping("/subjects")
    fun subjects(): ResponseEntity<List<Subject>> {
        val subjects = enrollmentRepository.subjects()
        return ResponseEntity.ok(subjects)
    }

    @GetMapping("/enrollments")
    fun enrollments(): ResponseEntity<List<Enrollment>> {
        val enrollments = enrollmentRepository.enrollments()
        return ResponseEntity.ok(enrollments)
    }

}