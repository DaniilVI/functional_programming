package ru.yarsu

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import ru.yarsu.data.LaunderetteReader
import ru.yarsu.data.LaunderetteStorage
import ru.yarsu.data.MachineReader
import ru.yarsu.data.MachineStorage
import ru.yarsu.data.UserReader
import ru.yarsu.data.UserStorage
import ru.yarsu.web.routes.ApplicationRoutes
import kotlin.collections.mutableListOf
import kotlin.concurrent.thread
import kotlin.system.exitProcess

// A
fun main(arguments: Array<String>) {
    var errorCode = 0
    val args = Args()
    val commander: JCommander =
        JCommander
            .newBuilder()
            .addObject(args)
            .build()
    try {
        commander.parse(*arguments)
    } catch (e: ParameterException) {
        if (args.csvMachineFile == null) {
            println("Error: missing option --machines-file")
            errorCode = 1
        } else {
            if (args.csvLaunderetteFile == null) {
                println("Error: missing option --launderettes-file")
                errorCode = 1
            } else {
                if (args.csvUserFile == null) {
                    println("Error: missing option --users-file")
                    errorCode = 1
                } else {
                    if (args.port == null) {
                        println("Error: missing option --port")
                        errorCode = 1
                    }
                }
            }
        }
    } catch (e: IllegalArgumentException) {
        println(e.message)
        errorCode = 1
    }
    val app = ApplicationRoutes()
    if (errorCode == 0) {
        val machineReader = MachineReader()
        val launderetteReader = LaunderetteReader()
        val userReader = UserReader()
        val csvMachineFile = args.csvMachineFile ?: ""
        val csvLaunderetteFile = args.csvLaunderetteFile ?: ""
        val csvUserFile = args.csvUserFile ?: ""
        if (machineReader.checkFile(csvMachineFile) == 1) {
            println("Error: file not found «$csvMachineFile»")
            errorCode = 1
        }
        if (launderetteReader.checkFile(csvLaunderetteFile) == 1) {
            println("Error: file not found «$csvLaunderetteFile»")
            errorCode = 1
        }
        if (userReader.checkFile(csvUserFile) == 1) {
            println("Error: file not found «$csvUserFile»")
            errorCode = 1
        }

        if (errorCode == 0) {
            machineReader.readData(csvMachineFile)
            launderetteReader.readData(csvLaunderetteFile)
            userReader.readData(csvUserFile)

            val storageUser = UserStorage(userReader.getData())
            val storageLaunderette = LaunderetteStorage(launderetteReader.getData())
            val storageMachine = MachineStorage(machineReader.getData())
            Runtime.getRuntime().addShutdownHook(
                thread(
                    start = false,
                    block = {
                        saveData(
                            storageMachine,
                            storageLaunderette,
                            storageUser,
                            csvMachineFile,
                            csvLaunderetteFile,
                            csvUserFile,
                        )
                    },
                ),
            )
            if (app.startServer(
                    args.port ?: 0,
                    storageMachine,
                    storageLaunderette,
                    storageUser,
                ) == 1
            ) {
                println("Incorrect port")
                commander.usage()
                errorCode = 1
            }
        }
    }
    if (errorCode == 1) {
        exitProcess(errorCode)
    }
}

// A
fun saveData(
    storageMachine: MachineStorage,
    storageLaunderette: LaunderetteStorage,
    storageUser: UserStorage,
    csvMachineFile: String,
    csvLaunderetteFile: String,
    csvUserFile: String,
) {
    val writer = csvWriter()
    writer.writeAll(getExitDataMachine(storageMachine), csvMachineFile)
    writer.writeAll(getExitDataLaunderette(storageLaunderette), csvLaunderetteFile)
    writer.writeAll(getExitDataUser(storageUser), csvUserFile)
}

// A
fun getExitDataMachine(storageMachine: MachineStorage): MutableList<List<String?>> {
    val listHeadersMachine =
        listOf(
            "Id",
            "Title",
            "Type",
            "StartDateTime",
            "Condition",
            "Capacity",
            "Price",
            "Duration",
            "Specialist",
            "Launderette",
        )
    val exitDataMachine = mutableListOf<List<String?>>()
    exitDataMachine.add(listHeadersMachine)
    storageMachine.getAll().values.forEach {
        exitDataMachine.add(it.getCSV())
    }
    return exitDataMachine
}

// A
fun getExitDataLaunderette(storageLaunderette: LaunderetteStorage): MutableList<List<String>> {
    val listHeadersLaunderette =
        listOf(
            "Id",
            "Address",
            "PhoneNumber",
            "WorkingHours",
        )
    val exitDataLaunderette = mutableListOf<List<String>>()
    exitDataLaunderette.add(listHeadersLaunderette)
    storageLaunderette.getAll().values.forEach {
        exitDataLaunderette.add(it.getCSV())
    }
    return exitDataLaunderette
}

// A
fun getExitDataUser(storageUser: UserStorage): MutableList<List<String>> {
    val listHeadersUser =
        listOf(
            "Id",
            "Name",
            "RegistrationDateTime",
            "Email",
        )
    val exitDataUser = mutableListOf<List<String>>()
    exitDataUser.add(listHeadersUser)
    storageUser.getAll().values.forEach {
        exitDataUser.add((it.getCSV()))
    }
    return exitDataUser
}
