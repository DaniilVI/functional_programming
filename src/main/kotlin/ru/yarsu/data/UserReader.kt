package ru.yarsu.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID.fromString

class UserReader {
    private val dataList = mutableListOf<User>()

    private fun createUser(args: List<String>): User {
        val user =
            User(
                fromString(args[0]),
                args[1],
                parse(args[2], ISO_LOCAL_DATE_TIME),
                args[3],
            )
        return user
    }

    fun readData(fileName: String): Int {
        if (fileName == "") {
            return 1
        }
        val file = File(fileName)
        val reader = csvReader()
        var rows: List<List<String>>
        try {
            rows = reader.readAll(file)
            rows = rows.drop(1)
        } catch (e: FileNotFoundException) {
            return 1
        }
        for (i in rows) {
            dataList.add(createUser(i))
        }
        return 0
    }

    fun getData(): MutableList<User> = dataList
}
