package ru.yarsu.operations

import ru.yarsu.data.UserStorage
import java.util.UUID

interface GetUserFieldOperation {
    fun getEmail(id: UUID): String
}

class GetUserFieldOperationImpl(
    private val userStorage: UserStorage,
) : GetUserFieldOperation {
    override fun getEmail(id: UUID): String {
        val user = userStorage.get(id)
        return user?.email ?: ""
    }
}
