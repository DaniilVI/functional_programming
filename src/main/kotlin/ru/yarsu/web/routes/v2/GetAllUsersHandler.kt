package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.data.User
import ru.yarsu.operations.GetAllUsersOperationImpl
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

class GetAllUsersHandler(
    private val jsonFactory: JsonNodeFactory,
    private val users: GetAllUsersOperationImpl,
) : HttpHandler {
    override fun invoke(request: Request): Response =
        Response(OK).with(
            jsonBodyLens of arrayUsers(users.getAll()),
        )

    private fun arrayUsers(listUsers: Collection<User>): JsonNode {
        val node = jsonFactory.arrayNode()
        listUsers.forEach {
            node.addObject().apply {
                put("Id", it.id.toString())
                put("Name", it.name)
                put("RegistrationDateTime", it.registrationDateTime.format(ISO_LOCAL_DATE_TIME))
                put("Email", it.email)
            }
        }
        return node
    }
}
