package ru.yarsu.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID.fromString
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.Failure

class UserReader {
    private val dataList = mutableListOf<User>()

    // C
    private fun createUser(args: List<String>): Result<User, String?> {
        try {
            val user =
                User(
                    fromString(args[0]),
                    args[1],
                    parse(args[2], ISO_LOCAL_DATE_TIME),
                    args[3],
                )
            return Success(user)
        } catch (e: Exception) {
            return Failure(e.message)
        }
    }

    // A
    fun readData(fileName: String) {
        val file = File(fileName)
        val reader = csvReader()
        val rows: List<List<String>> = reader.readAll(file).drop(1)
        for (i in rows) {
            when (val result = createUser(i)) {
                is Success -> {
                    dataList.add(result.value)
                }

                is Failure -> {
                    println("Error for user with id = ${i[0]}: ${result.reason}")
                }
            }
        }
    }

    // C
    fun checkFile(fileName: String): Int {
        if (fileName == "") {
            return 1
        }
        val file = File(fileName)
        if (!file.exists() || !file.isFile()) {
            return 1
        }
        return 0
    }

    // A
    fun getData(): MutableList<User> = dataList
}
