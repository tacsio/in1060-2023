package io.tacsio.adaptive.api

import io.tacsio.adaptive.api.model.Subject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EnrollmentController(private val enrollmentService: EnrollmentService) {

    @GetMapping("/")
    fun get(): ResponseEntity<*> {
        return ResponseEntity.ok("Hello")
    }


    @GetMapping("/suggestion/{studentId}")
    suspend fun suggestions(studentId: Long): ResponseEntity<Subject> {
        val student = enrollmentService.getStudent(studentId)

        student?.let {
            enrollmentService.getSuggestions(it)
        }

        return ResponseEntity.notFound().build()
    }

}