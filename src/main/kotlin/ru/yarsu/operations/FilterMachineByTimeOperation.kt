package ru.yarsu.operations

import ru.yarsu.data.Machine
import ru.yarsu.data.MachineStorage
import java.time.LocalDateTime

interface FilterMachineByTimeOperation {
    fun timeToChange(nowDataTime: LocalDateTime): Collection<Machine>

    fun wornOut(nowDataTime: LocalDateTime): Collection<Machine>

    fun works(nowDataTime: LocalDateTime): Collection<Machine>

    fun new(nowDataTime: LocalDateTime): Collection<Machine>
}

class FilterMachineByTimeOperationImpl(
    private val storage: MachineStorage,
) : FilterMachineByTimeOperation {
    override fun timeToChange(nowDataTime: LocalDateTime): Collection<Machine> =
        storage.getAll().values.filter { !it.condition || nowDataTime.minusYears(5) > it.startDateTime }

    override fun wornOut(nowDataTime: LocalDateTime): Collection<Machine> =
        storage.getAll().values.filter {
            it.condition &&
                nowDataTime.minusYears(3) >= it.startDateTime &&
                nowDataTime.minusYears(5) <= it.startDateTime
        }

    override fun works(nowDataTime: LocalDateTime): Collection<Machine> =
        storage.getAll().values.filter {
            it.condition &&
                nowDataTime.minusMonths(6) >= it.startDateTime &&
                nowDataTime.minusYears(3) < it.startDateTime
        }

    override fun new(nowDataTime: LocalDateTime): Collection<Machine> =
        storage.getAll().values.filter {
            it.condition && nowDataTime.minusMonths(6) < it.startDateTime
        }
}
