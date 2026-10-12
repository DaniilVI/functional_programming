package ru.yarsu.data

import java.util.UUID

class LaunderetteStorage(
    dataList: List<Launderette>,
) {
    private val list = mutableMapOf<UUID, Launderette>()

    init {
        dataList.forEach {
            list[it.id] = it
        }
    }

    // A
    fun getAll(): Map<UUID, Launderette> = list

    // A
    fun get(id: UUID): Launderette? = list.getOrDefault(id, null)

    // A
    fun add(launderette: Launderette): Int {
        if (!list.containsKey(launderette.id)) {
            list[launderette.id] = launderette
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
