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
    override fun get(type: Type): Collection<Machine> = storage.getAll().values.filter { it.type == type.russianName }
}
