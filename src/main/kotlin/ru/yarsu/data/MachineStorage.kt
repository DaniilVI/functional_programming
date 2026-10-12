package ru.yarsu.data

import java.util.UUID

class MachineStorage(
    dataList: List<Machine>,
) {
    private val list = mutableMapOf<UUID, Machine>()

    init {
        dataList.forEach {
            list[it.id] = it
        }
    }

    // A
    fun getAll(): Map<UUID, Machine> = list

    // A
    fun get(id: UUID): Machine? = list.getOrDefault(id, null)

    // A
    fun add(machine: Machine): Int {
        if (!list.containsKey(machine.id)) {
            list[machine.id] = machine
            return 0
        } else {
            return 1
        }
    }

    // A
    fun remove(id: UUID): Int =
        if (list.remove(id) == null) {
            1
        } else {
            0
        }
}
