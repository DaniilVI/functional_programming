package ru.yarsu.data

import java.time.LocalDateTime
import java.util.UUID

class User(
    override val id: UUID,
    val name: String,
    val registrationDateTime: LocalDateTime,
    private val email: String,
): Identifiable {
    // C
    override fun toString(): String =
        "Id: $id\n" +
            "Name: $name\n" +
            "RegistrationDateTime: $registrationDateTime\n" +
            "Email: $email"

    // C
    fun getCSV(): List<String> {
        val dataList = mutableListOf<String>()
        dataList.add(id.toString())
        dataList.add(name)
        dataList.add(registrationDateTime.toString())
        dataList.add(email)
        return dataList
    }

    // C
    fun getEmail(): String = email
}
