package ru.yarsu.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID.fromString
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.Failure

class MachineReader {
    private val dataList = mutableListOf<Machine>()

    // C
    private fun createMachine(args: List<String>): Result<Machine, String?> {
        try {
            val launderetteId = if (args[9] == "") null else fromString(args[9])
            val machine =
                Machine(
                    fromString(args[0]),
                    args[1],
                    args[2],
                    parse(args[3], ISO_LOCAL_DATE_TIME),
                    args[4].toBooleanStrict(),
                    args[5].toDouble(),
                    args[6].toInt(),
                    args[7].toInt(),
                    fromString(args[8]),
                    launderetteId,
                )
            return Success(machine)
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
            when (val result = createMachine(i)) {
                is Success -> {
                    dataList.add(result.value)
                }

                is Failure -> {
                    println("Error for machine with id = ${i[0]}: ${result.reason}")
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
    fun getData(): MutableList<Machine> = dataList
}
