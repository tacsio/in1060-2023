package io.tacsio.adaptive.api.model

import java.util.*

data class Student(val id: Long, val name: String)

data class Subject(val id: Long, val name: String)

data class Enrollment(val id: UUID, val student: Student, val subject: Subject)