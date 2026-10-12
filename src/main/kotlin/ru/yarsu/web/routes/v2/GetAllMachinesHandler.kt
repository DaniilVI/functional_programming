package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.pageableLens
import ru.yarsu.data.Machine
import ru.yarsu.operations.GetAllMachinesPagingOperationImpl

class GetAllMachinesHandler(
    private val jsonFactory: JsonNodeFactory,
    private val listMachine: GetAllMachinesPagingOperationImpl,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val pageable = pageableLens(request)
        return Response(OK).with(
            jsonBodyLens of correct(listMachine.getAll(pageable.page, pageable.recordsPerPage)),
        )
    }

    private fun correct(machineList: Collection<Machine>): JsonNode {
        val node = jsonFactory.arrayNode()
        machineList.forEach {
            node.addObject().apply {
                put("Id", it.id.toString())
                put("Title", it.title)
                put("Condition", it.condition)
            }
        }
        return node
    }
}
