package io.tacsio.adaptive.api

import com.github.javafaker.Faker
import io.tacsio.adaptive.api.model.Enrollment
import io.tacsio.adaptive.api.model.Student
import io.tacsio.adaptive.api.model.Subject
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class EnrollmentRepository {

    private var students = HashMap<Long, Student>()
    private var subjects = HashMap<Long, Subject>()
    private var enrollments = HashMap<UUID, Enrollment>()

    fun subjectsNotEnrolled(student: Student): List<Subject> {
        val enrolled = studentEnrollments(student).map { it.subject.id }

        runBlocking { delay((50L..100L).random()) }
        return subjects.values.filter { it.id !in enrolled }
    }

    fun studentEnrollments(student: Student): List<Enrollment> {
        return enrollments.values.filter { it.student.id == student.id }
    }

    fun saveEnrollment(enrollment: Enrollment) {
        enrollments[enrollment.id] = enrollment
    }

    fun findStudent(id: Long): Student? {
        return students[id]
    }

    fun findSubject(id: Long): Subject? {
        return subjects[id]
    }

    fun students(): List<Student> {
        return ArrayList(students.values)
    }

    fun subjects(): List<Subject> {
        return ArrayList(subjects.values)
    }

    fun enrollments(): List<Enrollment> {
        return ArrayList(enrollments.values)
    }

    @PostConstruct
    fun initData() {
        val faker = Faker()

        (1..20).forEach {
            val id = it.toLong()
            var name = faker.lordOfTheRings().character()

            while (students.values.any { student -> student.name == name }) {
                name = faker.lordOfTheRings().character()
            }

            students[id] = Student(id, name)
        }

        (1..50).forEach {
            val id = it.toLong()
            var lang = faker.programmingLanguage().name()

            while (subjects.values.any { subject -> subject.name == lang }) {
                lang = faker.programmingLanguage().name()
            }

            subjects[id] = Subject(id, lang, faker.programmingLanguage().creator())
        }
    }

}