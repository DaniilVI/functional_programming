package ru.yarsu.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID.fromString

class MachineReader {
    private val dataList = mutableListOf<Machine>()

    private fun createMachine(args: List<String>): Machine {
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
        return machine
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
            dataList.add(createMachine(i))
        }
        return 0
    }

    fun getData(): MutableList<Machine> = dataList
}
