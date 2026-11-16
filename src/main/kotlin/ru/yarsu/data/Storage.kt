package ru.yarsu.data

import java.util.UUID
import kotlin.collections.getOrDefault

open class Storage<T: Identifiable>(
    dataList: List<T>,
) {
    private val list = mutableMapOf<UUID, T>()

    init {
        dataList.forEach {
            list[it.id] = it
        }
    }

    // A
    fun getAll(): Collection<T> = list.toMap().values

    // A
    fun get(id: UUID): T? = list.getOrDefault(id, null)

    // A
    fun add(item: T): Int {
        if (!list.containsKey(item.id)) {
            list[item.id] = item
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

    // A
    fun contains(id: UUID): Boolean = list.containsKey(id)

    // A
    fun filter(predicate: (T) -> Boolean): List<T> = list.values.filter(predicate)
}
