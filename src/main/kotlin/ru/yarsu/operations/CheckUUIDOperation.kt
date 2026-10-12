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
    override fun check(id: UUID): Boolean {
        userStorage.getAll().values.forEach {
            if (it.id == id) {
                return true
            }
        }
        return false
    }
}

class CheckLaunderetteUUIDOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
) : CheckUUIDOperation {
    override fun check(id: UUID): Boolean {
        launderetteStorage.getAll().values.forEach {
            if (it.id == id) {
                return true
            }
        }
        return false
    }
}

class CheckMachineUUIDOperationImpl(
    private val machineStorage: MachineStorage,
) : CheckUUIDOperation {
    override fun check(id: UUID): Boolean {
        machineStorage.getAll().values.forEach {
            if (it.id == id) {
                return true
            }
        }
        return false
    }
}
