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
        return userStorage.contains(id)
    }
}

class CheckLaunderetteUUIDOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
) : CheckUUIDOperation {
    // A
    override fun check(id: UUID): Boolean {
        return launderetteStorage.contains(id)
    }
}

class CheckMachineUUIDOperationImpl(
    private val machineStorage: MachineStorage,
) : CheckUUIDOperation {
    // A
    override fun check(id: UUID): Boolean {
        return machineStorage.contains(id)
    }
}
