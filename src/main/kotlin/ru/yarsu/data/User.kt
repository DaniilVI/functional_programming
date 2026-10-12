package ru.yarsu.data

import java.time.LocalDateTime
import java.util.UUID

class User(
    val id: UUID,
    val name: String,
    val registrationDateTime: LocalDateTime,
    val email: String,
) {
    // A
    override fun toString(): String =
        "Id: $id\n" +
            "Name: $name\n" +
            "RegistrationDateTime: $registrationDateTime\n" +
            "Email: $email"

    // A
    fun getCSV(): List<String> {
        val dataList = mutableListOf<String>()
        dataList.add(id.toString())
        dataList.add(name)
        dataList.add(registrationDateTime.toString())
        dataList.add(email)
        return dataList
    }
}
