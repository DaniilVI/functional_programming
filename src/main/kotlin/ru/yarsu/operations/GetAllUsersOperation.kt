package ru.yarsu.operations

import ru.yarsu.data.User
import ru.yarsu.data.UserStorage

interface GetAllUsersOperation {
    fun getAll(): Collection<User>
}

class GetAllUsersOperationImpl(
    private val storage: UserStorage,
) : GetAllUsersOperation {
    override fun getAll(): Collection<User> = storage.getAll().values.sortedWith(compareBy<User> { it.name }.thenBy { it.id })
}
