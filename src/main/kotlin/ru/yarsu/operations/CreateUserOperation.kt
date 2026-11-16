package ru.yarsu.operations

import ru.yarsu.data.User
import ru.yarsu.data.UserStorage
import java.time.LocalDateTime
import java.util.UUID

interface CreateUserOperation {
    fun createUser(
        name: String,
        email: String,
        registrationDateTime: LocalDateTime,
    ): UUID?
}

class CreateUserOperationImpl(
    private val userStorage: UserStorage,
) : CreateUserOperation {
    override fun createUser(
        name: String,
        email: String,
        registrationDateTime: LocalDateTime,
    ): UUID? {
        userStorage.getAll().forEach {
            if (it.getEmail() == email) {
                return null
            }
        }
        var userId = UUID.randomUUID()
        while (userStorage.contains(userId)) {
            userId = UUID.randomUUID()
        }
        val user =
            User(
                userId,
                name,
                registrationDateTime,
                email,
            )
        userStorage.add(user)
        return userId
    }
}
