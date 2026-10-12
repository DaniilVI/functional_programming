package ru.yarsu.operations

import ru.yarsu.data.MachineStorage
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID
import java.util.UUID.fromString

interface EditMachineOperation {
    fun editMachine(
        id: UUID,
        title: String?,
        type: String?,
        startDateTime: String?,
        condition: String?,
        capacity: String?,
        price: String?,
        duration: String?,
        specialist: String?,
        launderette: String?,
    )
}

class EditMachineOperationImpl(
    private val machineStorage: MachineStorage,
) : EditMachineOperation {
    override fun editMachine(
        id: UUID,
        title: String?,
        type: String?,
        startDateTime: String?,
        condition: String?,
        capacity: String?,
        price: String?,
        duration: String?,
        specialist: String?,
        launderette: String?,
    ) {
        val machine = machineStorage.get(id)
        if (title != null) {
            machine?.title = title
        }
        if (type != null) {
            machine?.type = type
        }
        if (startDateTime != null) {
            machine?.startDateTime = parse(startDateTime, ISO_LOCAL_DATE_TIME)
        }
        if (condition != null) {
            machine?.condition = condition.toBooleanStrict()
        }
        if (capacity != null) {
            machine?.capacity = capacity.toDouble()
        }
        if (price != null) {
            machine?.price = price.toInt()
        }
        if (duration != null) {
            machine?.duration = duration.toInt()
        }
        if (specialist != null) {
            machine?.specialist = fromString(specialist)
        }
        if (launderette != null) {
            machine?.launderette = fromString(launderette)
        }
    }
}
