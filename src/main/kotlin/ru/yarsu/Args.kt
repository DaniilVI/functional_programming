package ru.yarsu

import com.beust.jcommander.Parameter

class Args {
    @Parameter(
        names = ["--machines-file"],
        description = "The source of machines from .csv file",
        required = true,
    )
    var csvMachineFile: String? = null

    @Parameter(
        names = ["--launderettes-file"],
        description = "The source of launderettes from .csv file",
        required = true,
    )
    var csvLaunderetteFile: String? = null

    @Parameter(
        names = ["--users-file"],
        description = "The source of users from .csv file",
        required = true,
    )
    var csvUserFile: String? = null

    @Parameter(
        names = ["--port"],
        description = "The number of http port",
        required = true,
    )
    var port: Int? = null
}
