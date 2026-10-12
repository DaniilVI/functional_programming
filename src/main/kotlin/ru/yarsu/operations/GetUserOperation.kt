package ru.yarsu.operations

import ru.yarsu.data.User
import ru.yarsu.data.UserStorage

interface GetUserOperation {
    fun get(email: String): User?
}

class GetUserFromEmailOperationImpl(
    private val userStorage: UserStorage,
) : GetUserOperation {
    override fun get(email: String): User? {
        userStorage.getAll().values.forEach {
            if (it.email == email) {
                return it
            }
        }
        return null
    }
}
