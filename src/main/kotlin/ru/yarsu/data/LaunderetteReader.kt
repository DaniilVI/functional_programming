package ru.yarsu.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import dev.forkhandles.result4k.Failure
import java.util.UUID.fromString

class LaunderetteReader {
    private val dataList = mutableListOf<Launderette>()

    // С
    private fun createLaunderette(args: List<String>): Result<Launderette, Int> {
        try {
            val launderette =
                Launderette(
                    fromString(args[0]),
                    args[1],
                    args[2],
                    args[3],
                )

            return Success(launderette)
        } catch (_: Exception) {
            return Failure(-1)
        }
    }

    // A
    fun readData(fileName: String) {
        val file = File(fileName)
        val reader = csvReader()
        val rows: List<List<String>> = reader.readAll(file).drop(1)
        for (i in rows) {
            when (val result = createLaunderette(i)) {
                is Success -> {
                    dataList.add(result.value)
                }

                is Failure -> {
                    println("Error: cannot create launderette with id: ${i[0]}")
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
    fun getData(): MutableList<Launderette> = dataList
}
