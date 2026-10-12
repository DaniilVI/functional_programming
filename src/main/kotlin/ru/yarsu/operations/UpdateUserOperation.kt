package ru.yarsu.operations

import ru.yarsu.data.MachineStorage
import java.util.UUID

interface UpdateUserOperation {
    fun update(
        idFrom: UUID,
        idTo: UUID,
    )
}

class UpdateUserOperationImpl(
    private val machineStorage: MachineStorage,
) : UpdateUserOperation {
    override fun update(
        idFrom: UUID,
        idTo: UUID,
    ) {
        machineStorage.getAll().values.forEach {
            if (it.specialist == idFrom) {
                it.specialist = idTo
            }
        }
    }
}
