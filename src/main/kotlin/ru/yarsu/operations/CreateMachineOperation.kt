package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage
import java.time.LocalDateTime
import java.util.UUID

interface CreateMachineOperation {
    fun createMachine(
        title: String,
        type: String,
        startDateTime: LocalDateTime,
        condition: Boolean,
        capacity: Double,
        price: Int,
        duration: Int,
        specialist: UUID,
        launderette: UUID?,
    ): UUID
}

class CreateMachineOperationImpl(
    private val machineStorage: MachineStorage,
) : CreateMachineOperation {
    override fun createMachine(
        title: String,
        type: String,
        startDateTime: LocalDateTime,
        condition: Boolean,
        capacity: Double,
        price: Int,
        duration: Int,
        specialist: UUID,
        launderette: UUID?,
    ): UUID {
        var machineId = UUID.randomUUID()
        while (machineStorage.getAll().containsKey(machineId)) {
            machineId = UUID.randomUUID()
        }
        val machine =
            Machine(
                machineId,
                title,
                type,
                startDateTime,
                condition,
                capacity,
                price,
                duration,
                specialist,
                launderette,
            )
        machineStorage.add(machine)
        return machineId
    }
}
