package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.CONFLICT
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.format.Jackson
import ru.yarsu.Lens.addUserLens
import ru.yarsu.Lens.emailField
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.nameField
import ru.yarsu.Lens.registrationDateTimeField
import ru.yarsu.data.User
import ru.yarsu.operations.CreateUserOperationImpl
import ru.yarsu.operations.GetIncorrectValueOperationImpl
import ru.yarsu.operations.GetUserFromEmailOperationImpl
import java.util.UUID

class AddNewUserHandler(
    private val jsonFactory: JsonNodeFactory,
    private val newUser: CreateUserOperationImpl,
    private val user: GetUserFromEmailOperationImpl,
    private val errorValue: GetIncorrectValueOperationImpl,
) : HttpHandler {
    private val json = Jackson

    override fun invoke(request: Request): Response {
        val form = addUserLens(request)
        if (form.errors.isEmpty()) {
            val name = nameField(form)
            val email = emailField(form)
            val registrationDateTime = registrationDateTimeField(form)
            val userId: UUID? = newUser.createUser(name, email, registrationDateTime)
            return if (userId == null) {
                Response(CONFLICT).with(
                    jsonBodyLens of incorrectEmail(user.get(email)),
                )
            } else {
                Response(CREATED).with(
                    jsonBodyLens of correctJson(userId),
                )
            }
        }
        val errorMap = errorValue.get(form)
        return Response(BAD_REQUEST).with(
            jsonBodyLens of
                incorrectJson(
                    errorMap.getOrDefault("Name", null),
                    errorMap.getOrDefault("Email", null),
                    errorMap.getOrDefault("RegistrationDateTime", null),
                ),
        )
    }

    private fun correctJson(userId: UUID): JsonNode =
        json {
            obj(
                "Id" to string(userId.toString()),
            )
        }

    private fun incorrectEmail(user: User?): JsonNode =
        json {
            obj(
                "Id" to string(user?.id.toString()),
                "Name" to string(user?.name.toString()),
                "Email" to string(user?.email.toString()),
            )
        }

    private fun incorrectJson(
        name: String?,
        email: String?,
        registrationDateTime: String?,
    ): JsonNode {
        val node = jsonFactory.objectNode()
        if (name == "") {
            node.putObject("Name").apply {
                putNull("Value")
                put("Error", "Отсутствует поле")
            }
        }
        if (email == "") {
            node.putObject("Email").apply {
                putNull("Value")
                put("Error", "Отсутствует поле")
            }
        }
        if (registrationDateTime != null) {
            node.putObject("RegistrationDateTime").apply {
                put("Value", registrationDateTime)
                put("Error", "Ожидается дата и время")
            }
        }
        return node
    }
}
