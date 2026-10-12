package ru.yarsu.operations

import ru.yarsu.data.LaunderetteStorage
import ru.yarsu.data.MachineStorage
import java.util.UUID

interface DeleteLaunderetteOperation {
    fun delete(id: UUID): Int
}

class DeleteLaunderetteOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
    private val machineStorage: MachineStorage,
) : DeleteLaunderetteOperation {
    override fun delete(id: UUID): Int {
        if (launderetteStorage.remove(id) == 0) {
            machineStorage.getAll().values.forEach {
                if (it.launderette == id) {
                    it.launderette = null
                }
            }
            return 0
        } else {
            return 1
        }
    }
}
