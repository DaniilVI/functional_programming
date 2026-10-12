package ru.yarsu.data

import java.time.LocalDateTime
import java.util.UUID

class Machine(
    val id: UUID,
    var title: String,
    var type: String,
    var startDateTime: LocalDateTime,
    var condition: Boolean,
    var capacity: Double,
    var price: Int,
    var duration: Int,
    var specialist: UUID,
    var launderette: UUID?,
) {
    override fun toString(): String =
        "id = $id," +
            " title = $title," +
            " type = $type," +
            " startDateTime = $startDateTime," +
            " condition = $condition," +
            " capacity = $capacity," +
            " price = $price" +
            " duration = $duration" +
            " specialist = $specialist" +
            " launderette = $launderette"

    fun getCSV(): List<String?> {
        val dataList = mutableListOf<String?>()
        dataList.add(id.toString())
        dataList.add(title)
        dataList.add(type)
        dataList.add(startDateTime.toString())
        dataList.add(condition.toString())
        dataList.add(capacity.toString())
        dataList.add(price.toString())
        dataList.add(duration.toString())
        dataList.add(specialist.toString())
        dataList.add(launderette?.toString())
        return dataList
    }
}
