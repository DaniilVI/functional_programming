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

    fun getAll(): Map<UUID, Launderette> = list

    fun get(id: UUID): Launderette? = list.getOrDefault(id, null)

    fun add(launderette: Launderette): Int {
        if (!list.containsKey(launderette.id)) {
            list[launderette.id] = launderette
            return 0
        } else {
            return 1
        }
    }

    fun remove(id: UUID): Int =
        if (list.remove(id) == null) {
            1
        } else {
            0
        }
}
