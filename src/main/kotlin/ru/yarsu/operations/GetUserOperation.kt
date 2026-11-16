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
        userStorage.getAll().forEach {
            if (it.getEmail() == email) {
                return it
            }
        }
        return null
    }
}
