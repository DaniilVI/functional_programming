package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import org.http4k.format.Jackson
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.machineIdLens
import ru.yarsu.data.Machine
import ru.yarsu.operations.GetLaunderetteFieldOperationImpl
import ru.yarsu.operations.GetMachineOperationImpl
import ru.yarsu.operations.GetUserFieldOperationImpl
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID

class GetMachineHandler(
    private val jsonFactory: JsonNodeFactory,
    private val machines: GetMachineOperationImpl,
    private val userField: GetUserFieldOperationImpl,
    private val launderetteField: GetLaunderetteFieldOperationImpl,
) : HttpHandler {
    private val json = Jackson

    override fun invoke(request: Request): Response {
        val machineId = machineIdLens(request)
        val machine = machines.get(machineId)
        return if (machine != null) {
            Response(OK).with(
                jsonBodyLens of
                    correct(
                        machine,
                        userField.getEmail(machine.specialist),
                        launderetteField.getAddress(machine.launderette),
                    ),
            )
        } else {
            Response(NOT_FOUND).with(
                jsonBodyLens of incorrect(machineId),
            )
        }
    }

    private fun correct(
        machine: Machine,
        email: String,
        address: String?,
    ): JsonNode {
        val node = jsonFactory.objectNode()
        node.apply {
            put("Id", machine.id.toString())
            put("Title", machine.title)
            put("Type", machine.type)
            put("StartDateTime", machine.startDateTime.format(ISO_LOCAL_DATE_TIME))
            put("Condition", machine.condition)
            put("Capacity", machine.capacity)
            put("Price", machine.price)
            put("Duration", machine.duration)
            put("SpecialistEmail", email)
        }
        if (address != null) {
            node.apply {
                put("LaunderetteAddress", address)
            }
        } else {
            node.apply {
                putNull("LaunderetteAddress")
            }
        }
        return node
    }

    private fun incorrect(machineId: UUID): JsonNode =
        json {
            obj(
                "MachineId" to string(machineId.toString()),
                "Error" to string("Оборудование не найдено"),
            )
        }
}
