package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage
import java.util.UUID

interface GetMachineOperation {
    fun get(id: UUID): Machine?
}

class GetMachineOperationImpl(
    private val machineStorage: MachineStorage,
) : GetMachineOperation {
    override fun get(id: UUID): Machine? {
        machineStorage.getAll().forEach {
            if (it.key == id) {
                return it.value
            }
        }
        return null
    }
}
