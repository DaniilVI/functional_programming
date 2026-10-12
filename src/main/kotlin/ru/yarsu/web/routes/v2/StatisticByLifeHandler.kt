package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.timeNowLens
import ru.yarsu.operations.FilterMachineByTimeOperationImpl

class StatisticByLifeHandler(
    private val jsonFactory: JsonNodeFactory,
    private val filter: FilterMachineByTimeOperationImpl,
) : HttpHandler {
    override fun invoke(request: Request): Response {
        val time = timeNowLens(request)
        return Response(OK).with(
            jsonBodyLens of
                correct(
                    filter.timeToChange(time).size,
                    filter.wornOut(time).size,
                    filter.works(time).size,
                    filter.new(time).size,
                ),
        )
    }

    private fun correct(
        timeToChange: Int,
        wornOut: Int,
        works: Int,
        new: Int,
    ): JsonNode {
        val node = jsonFactory.objectNode()
        if (timeToChange != 0) {
            node.put("пора менять", timeToChange)
        }
        if (wornOut != 0) {
            node.put("изношено", wornOut)
        }
        if (works != 0) {
            node.put("работает", works)
        }
        if (new != 0) {
            node.put("новое", new)
        }
        return node
    }
}
