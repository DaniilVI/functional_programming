package ru.yarsu.operations

import ru.yarsu.data.LaunderetteStorage
import java.util.UUID

interface GetLaunderetteFieldOperation {
    fun getAddress(id: UUID?): String?
}

class GetLaunderetteFieldOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
) : GetLaunderetteFieldOperation {
    override fun getAddress(id: UUID?): String? {
        if (id == null) {
            return null
        } else {
            launderetteStorage.getAll().values.forEach {
                if (it.id == id) {
                    return it.address
                }
            }
            return null
        }
    }
}
