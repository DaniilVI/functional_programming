package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage
import java.util.UUID

interface FilterMachinesByWeightAndBudgetOperation {
    fun filterMachine(
        launderetteId: UUID,
        weight: Double,
        budget: Int,
    ): Collection<Machine>
}

class FilterMachinesByWeightAndBudgetOperationImpl(
    private val storage: MachineStorage,
) : FilterMachinesByWeightAndBudgetOperation {
    override fun filterMachine(
        launderetteId: UUID,
        weight: Double,
        budget: Int,
    ): Collection<Machine> =
        storage
            .getAll()
            .values
            .filter {
                it.launderette == launderetteId &&
                    it.condition &&
                    it.capacity >= weight &&
                    it.price <= budget
            }.sortedWith(compareBy<Machine> { it.price }.thenBy { it.duration }.thenBy { it.id })
}
