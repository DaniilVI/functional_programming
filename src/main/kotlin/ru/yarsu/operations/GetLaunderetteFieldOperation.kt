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
            val launderette = launderetteStorage.get(id)
            return launderette?.getAddress()
        }
    }
}
