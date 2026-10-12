package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.with
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.userIdFromLens
import ru.yarsu.Lens.userIdToLens
import ru.yarsu.operations.CheckUserUUIDOperationImpl
import ru.yarsu.operations.UpdateUserOperationImpl
import java.util.UUID

class ChangeUserHandler(
    private val jsonFactory: JsonNodeFactory,
    private val checker: CheckUserUUIDOperationImpl,
    private val updater: UpdateUserOperationImpl,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val idTo = userIdToLens(request)
        val idFrom = userIdFromLens(request)
        val to = checker.check(idTo)
        val from = checker.check(idFrom)
        if (to && from) {
            updater.update(idFrom, idTo)
            return Response(NO_CONTENT)
        } else {
            return Response(NOT_FOUND).with(
                jsonBodyLens of unknownUUID(to, idTo, from, idFrom),
            )
        }
    }

    private fun unknownUUID(
        to: Boolean,
        idTo: UUID,
        from: Boolean,
        idFrom: UUID,
    ): JsonNode {
        val node = jsonFactory.objectNode()
        if (!from) {
            node.apply {
                put("UserIdFrom", idFrom.toString())
            }
        }
        if (!to) {
            node.apply {
                put("UserIdTo", idTo.toString())
            }
        }
        return node
    }
}
