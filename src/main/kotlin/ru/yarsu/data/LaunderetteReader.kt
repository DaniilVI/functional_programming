package ru.yarsu.data

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import java.io.FileNotFoundException
import java.util.UUID.fromString

class LaunderetteReader {
    private val dataList = mutableListOf<Launderette>()

    private fun createLaunderette(args: List<String>): Launderette {
        val launderette =
            Launderette(
                fromString(args[0]),
                args[1],
                args[2],
                args[3],
            )
        return launderette
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
            dataList.add(createLaunderette(i))
        }
        return 0
    }

    fun getData(): MutableList<Launderette> = dataList
}
