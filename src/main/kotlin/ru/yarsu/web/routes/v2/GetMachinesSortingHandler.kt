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
import ru.yarsu.Lens.budgetLens
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.launderetteIdLens
import ru.yarsu.Lens.weightLens
import ru.yarsu.data.Machine
import ru.yarsu.operations.CheckLaunderetteUUIDOperationImpl
import ru.yarsu.operations.FilterMachinesByWeightAndBudgetOperationImpl
import java.util.UUID

class GetMachinesSortingHandler(
    private val jsonFactory: JsonNodeFactory,
    private val checker: CheckLaunderetteUUIDOperationImpl,
    private val filter: FilterMachinesByWeightAndBudgetOperationImpl,
) : HttpHandler {
    private val json = Jackson

    override fun invoke(request: Request): Response {
        val launderetteId = launderetteIdLens(request)
        val weight = weightLens(request)
        val budget = budgetLens(request)
        return if (checker.check(launderetteId)) {
            Response(OK).with(
                jsonBodyLens of
                    correct(
                        filter.filterMachine(
                            launderetteId,
                            weight,
                            budget,
                        ),
                    ),
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
                put("Capacity", it.capacity)
                put("Price", it.price)
                put("Duration", it.duration)
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
