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
import ru.yarsu.Lens.launderetteIdLens
import ru.yarsu.data.Machine
import ru.yarsu.operations.DeleteLaunderetteOperationImpl
import ru.yarsu.operations.GetAllMachinesOperationImpl
import java.util.UUID

class DeleteLaunderetteHandler(
    private val jsonFactory: JsonNodeFactory,
    private val deleter: DeleteLaunderetteOperationImpl,
    private val listMachine: GetAllMachinesOperationImpl,
) : HttpHandler {
    private val json = Jackson

    override fun invoke(request: Request): Response {
        val launderetteId = launderetteIdLens(request)
        return if (deleter.delete(launderetteId) == 0) {
            Response(OK).with(
                jsonBodyLens of correct(listMachine.getAll()),
            )
        } else {
            Response(NOT_FOUND).with(
                jsonBodyLens of incorrect(launderetteId),
            )
        }
    }

    private fun correct(machineList: Collection<Machine>): JsonNode {
        val node = jsonFactory.arrayNode()
        machineList.forEach {
            node.addObject().apply {
                put("Id", it.id.toString())
                put("Title", it.title)
            }
        }
        return node
    }

    private fun incorrect(launderetteId: UUID): JsonNode =
        json {
            obj(
                "LaunderetteId" to string(launderetteId.toString()),
                "Error" to string("Прачечная не найдена"),
            )
        }
}
