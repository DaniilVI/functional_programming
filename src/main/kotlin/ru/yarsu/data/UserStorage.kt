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

    fun getAll(): Map<UUID, User> = list

    fun get(id: UUID): User? = list.getOrDefault(id, null)

    fun add(user: User): Int {
        if (!list.containsKey(user.id)) {
            list[user.id] = user
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
