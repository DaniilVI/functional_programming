package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage
import ru.yarsu.data.Type

interface FilterMachineByTypeOperation {
    fun get(type: Type): Collection<Machine>
}

class FilterMachineByTypeOperationImpl(
    private val storage: MachineStorage,
) : FilterMachineByTypeOperation {
    // A
    override fun get(type: Type): Collection<Machine> = storage.filter { it.type == type.russianName }
}
