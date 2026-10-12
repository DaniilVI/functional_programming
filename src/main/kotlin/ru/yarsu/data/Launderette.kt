package ru.yarsu.data

import java.util.UUID

class Launderette(
    val id: UUID,
    val address: String,
    val phoneNumber: String,
    val workingHours: String,
) {
    // A
    override fun toString(): String =
        "Id = $id\n" +
            "Address = $address\n" +
            "PhoneNumber = $phoneNumber\n" +
            "WorkingHours = $workingHours\n"
    // A
    fun getCSV(): List<String> {
        val dataList = mutableListOf<String>()
        dataList.add(id.toString())
        dataList.add(address)
        dataList.add(phoneNumber)
        dataList.add(workingHours)
        return dataList
    }
}
