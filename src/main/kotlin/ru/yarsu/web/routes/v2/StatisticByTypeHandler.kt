package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.core.with
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.data.Type
import ru.yarsu.operations.FilterMachineByTypeOperationImpl

class StatisticByTypeHandler(
    private val jsonFactory: JsonNodeFactory,
    private val filter: FilterMachineByTypeOperationImpl,
) : HttpHandler {
    override fun invoke(request: Request): Response =
        Response(OK).with(
            jsonBodyLens of correct(filter),
        )

    private fun correct(filter: FilterMachineByTypeOperationImpl): JsonNode {
        val node = jsonFactory.arrayNode()
        for (type in Type.entries.sortedBy { it.russianName }) {
            val machineList = filter.get(type)
            if (machineList.isNotEmpty()) {
                val machineServiceableList = machineList.filter { it.condition }
                val nodeCapacity = jsonFactory.arrayNode()
                var capacity: Double? = null
                for (i in machineServiceableList.sortedBy { it.capacity }) {
                    if (i.capacity != capacity) {
                        capacity = i.capacity
                        val count = machineServiceableList.count { it.capacity == capacity }
                        val price =
                            machineServiceableList
                                .filter { it.capacity == capacity }
                                .sumOf { it.price }
                                .toDouble() / count.toDouble()
                        nodeCapacity.addObject().apply {
                            put("Capacity", capacity)
                            put("CountServiceable", count)
                            put("Price", price)
                        }
                    }
                }
                node.addObject().apply {
                    put("Type", type.russianName)
                    put("Count", machineList.size)
                    put("CountServiceable", machineServiceableList.size)
                    set<ArrayNode>("CapacityStatistic", nodeCapacity)
                }
            }
        }
        return node
    }
}
