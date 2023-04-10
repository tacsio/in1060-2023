package io.tacsio.adaptive.api

import io.tacsio.adaptive.api.model.Enrollment
import io.tacsio.adaptive.api.model.Student
import io.tacsio.adaptive.api.model.Subject
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EnrollmentRepository {

    @PostConstruct
    fun initData() {
        students[1] = Student(1, "Bob")
        students[2] = Student(2, "Alice")
        students[3] = Student(2, "Ana")

        subjects[1] = Subject(1, "Adaptive Systems")
        subjects[2] = Subject(1, "Software Architecture")
        subjects[3] = Subject(1, "Machine Learning")
    }

    var students = HashMap<Long, Student>()
    var subjects = HashMap<Long, Subject>()
    var enrollments = HashMap<UUID, Enrollment>()

    fun getSubjects(student: Student): List<Subject> {
        val enrolled = getEnrollments(student).map { it.subject.id }
        return subjects.values.filter { it.id !in enrolled }
    }

    fun getEnrollments(): List<Enrollment> {
        return ArrayList(enrollments.values)
    }

    fun getEnrollments(student: Student): List<Enrollment> {
        return enrollments.values.filter { it.student.id == student.id }
    }

    fun saveEnrollment(enrollment: Enrollment) {
        enrollments[enrollment.id] = enrollment
    }

    fun findStudent(id: Long): Student? {
        return students[id]
    }
}