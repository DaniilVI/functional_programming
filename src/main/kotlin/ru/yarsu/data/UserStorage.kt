package ru.yarsu.data

import java.util.UUID

class UserStorage(
    dataList: List<User>,
) {
    private val list = mutableMapOf<UUID, User>()

    init {
        dataList.forEach {
            list[it.id] = it
        }
    }

    // A
    fun getAll(): Map<UUID, User> = list

    // A
    fun get(id: UUID): User? = list.getOrDefault(id, null)

    // A
    fun add(user: User): Int {
        if (!list.containsKey(user.id)) {
            list[user.id] = user
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
