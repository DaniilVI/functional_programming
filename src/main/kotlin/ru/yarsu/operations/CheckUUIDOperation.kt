package ru.yarsu.operations

import ru.yarsu.data.LaunderetteStorage
import ru.yarsu.data.MachineStorage
import ru.yarsu.data.UserStorage
import java.util.UUID

interface CheckUUIDOperation {
    fun check(id: UUID): Boolean
}

class CheckUserUUIDOperationImpl(
    private val userStorage: UserStorage,
) : CheckUUIDOperation {
    // A
    override fun check(id: UUID): Boolean {
        return checkInMap(id, userStorage.getAll())
    }
}

class CheckLaunderetteUUIDOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
) : CheckUUIDOperation {
    // A
    override fun check(id: UUID): Boolean {
        return checkInMap(id, launderetteStorage.getAll())
    }
}

class CheckMachineUUIDOperationImpl(
    private val machineStorage: MachineStorage,
) : CheckUUIDOperation {
    // A
    override fun check(id: UUID): Boolean {
        return checkInMap(id, machineStorage.getAll())
    }
}

// C
fun checkInMap(id: UUID, storage: Map<UUID, Any>): Boolean {
    return storage.containsKey(id)
}

