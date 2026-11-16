package ru.yarsu.data

import java.util.UUID

class Launderette(
    override val id: UUID,
    private val address: String,
    val phoneNumber: String,
    val workingHours: String,
): Identifiable {
    // C
    override fun toString(): String =
        "Id = $id\n" +
            "Address = $address\n" +
            "PhoneNumber = $phoneNumber\n" +
            "WorkingHours = $workingHours\n"
    // C
    fun getCSV(): List<String> {
        val dataList = mutableListOf<String>()
        dataList.add(id.toString())
        dataList.add(address)
        dataList.add(phoneNumber)
        dataList.add(workingHours)
        return dataList
    }

    // C
    fun getAddress(): String = address
}
