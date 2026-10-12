package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage

interface GetAllMachinesOperation {
    fun getAll(): Collection<Machine>
}

class GetAllMachinesOperationImpl(
    private val storage: MachineStorage,
) : GetAllMachinesOperation {
    override fun getAll(): Collection<Machine> = storage.getAll().values.sortedWith(compareBy<Machine> { it.id })
}
