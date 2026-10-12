package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage

interface GetAllMachinesPagingOperation {
    fun getAll(
        page: Int = 1,
        count: Int = 10,
    ): Collection<Machine>
}

class GetAllMachinesPagingOperationImpl(
    private val storage: MachineStorage,
) : GetAllMachinesPagingOperation {
    override fun getAll(
        page: Int,
        count: Int,
    ): Collection<Machine> =
        storage
            .getAll()
            .values
            .sortedWith(compareBy<Machine> { it.startDateTime }.thenBy { it.id })
            .filterIndexed { index, _ ->
                index >= (page - 1) * count && index < page * count
            }
}
