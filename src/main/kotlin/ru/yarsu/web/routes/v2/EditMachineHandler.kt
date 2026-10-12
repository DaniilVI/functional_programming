package ru.yarsu.web.routes.v2

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.BAD_REQUEST
import org.http4k.core.Status.Companion.NOT_FOUND
import org.http4k.core.Status.Companion.NO_CONTENT
import org.http4k.core.with
import org.http4k.format.Jackson
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Lens.machineIdLens
import ru.yarsu.data.json.AddMachine
import ru.yarsu.operations.CheckMachineUUIDOperationImpl
import ru.yarsu.operations.EditMachineOperationImpl
import ru.yarsu.operations.GetJsonEditMachineFieldsOperationImpl
import java.util.UUID

class EditMachineHandler(
    private val jsonFactory: JsonNodeFactory,
    private val fields: GetJsonEditMachineFieldsOperationImpl,
    private val checker: CheckMachineUUIDOperationImpl,
    private val editor: EditMachineOperationImpl,
) : HttpHandler {
    private val json = Jackson
    private val mapper =
        jacksonObjectMapper().configure(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            false,
        )

    override fun invoke(request: Request): Response {
        val machineTemplate = mapper.readValue<AddMachine>(request.bodyString())
        val titleField = fields.title(machineTemplate.title)
        val title = titleField[0]
        val titleError = titleField[1]
        val typeField = fields.type(machineTemplate.type)
        val type = typeField[0]
        val typeError = typeField[1]
        val startDateTimeField = fields.startDateTime(machineTemplate.startDateTime)
        val startDateTime = startDateTimeField[0]
        val startDateTimeError = startDateTimeField[1]
        val conditionField = fields.condition(machineTemplate.condition)
        val condition = conditionField[0]
        val conditionError = conditionField[1]
        val capacityField = fields.capacity(machineTemplate.capacity)
        val capacity = capacityField[0]
        val capacityError = capacityField[1]
        val priceField = fields.price(machineTemplate.price)
        val price = priceField[0]
        val priceError = priceField[1]
        val durationField = fields.duration(machineTemplate.duration)
        val duration = durationField[0]
        val durationError = durationField[1]
        val specialistField = fields.specialist(machineTemplate.specialist)
        val specialist = specialistField[0]
        val specialistError = specialistField[1]
        val launderetteField = fields.launderette(machineTemplate.launderette)
        val launderette = launderetteField[0]
        val launderetteError = launderetteField[1]
        if (
            titleError == null &&
            typeError == null &&
            startDateTimeError == null &&
            conditionError == null &&
            capacityError == null &&
            priceError == null &&
            durationError == null &&
            specialistError == null &&
            launderetteError == null
        ) {
            val machineId = machineIdLens(request)
            if (checker.check(machineId)) {
                editor.editMachine(
                    machineId,
                    title,
                    type,
                    startDateTime,
                    condition,
                    capacity,
                    price,
                    duration,
                    specialist,
                    launderette,
                )
                return Response(NO_CONTENT)
            } else {
                return Response(NOT_FOUND).with(
                    jsonBodyLens of incorrectId(machineId),
                )
            }
        } else {
            return Response(BAD_REQUEST).with(
                jsonBodyLens of
                    incorrectJson(
                        title,
                        titleError,
                        type,
                        typeError,
                        startDateTime,
                        startDateTimeError,
                        condition,
                        conditionError,
                        capacity,
                        capacityError,
                        price,
                        priceError,
                        duration,
                        durationError,
                        specialist,
                        specialistError,
                        launderette,
                        launderetteError,
                    ),
            )
        }
    }

    private fun incorrectId(machineId: UUID): JsonNode =
        json {
            obj(
                "MachineId" to string(machineId.toString()),
                "Error" to string("Оборудование не найдено"),
            )
        }

    private fun incorrectJson(
        title: String?,
        titleError: String?,
        type: String?,
        typeError: String?,
        startDateTime: String?,
        startDateTimeError: String?,
        condition: String?,
        conditionError: String?,
        capacity: String?,
        capacityError: String?,
        price: String?,
        priceError: String?,
        duration: String?,
        durationError: String?,
        specialist: String?,
        specialistError: String?,
        launderette: String?,
        launderetteError: String?,
    ): JsonNode {
        val node = jsonFactory.objectNode()
        if (titleError != null) {
            node.putObject("Title").apply {
                put("Value", title)
                put("Error", titleError)
            }
        }
        if (typeError != null) {
            node.putObject("Type").apply {
                put("Value", type)
                put("Error", typeError)
            }
        }
        if (startDateTimeError != null) {
            node.putObject("StartDateTime").apply {
                put("Value", startDateTime)
                put("Error", startDateTimeError)
            }
        }
        if (conditionError != null) {
            node.putObject("Condition").apply {
                put("Value", condition)
                put("Error", conditionError)
            }
        }
        if (capacityError != null) {
            node.putObject("Capacity").apply {
                put("Value", capacity)
                put("Error", capacityError)
            }
        }
        if (priceError != null) {
            node.putObject("Price").apply {
                put("Value", price)
                put("Error", priceError)
            }
        }
        if (durationError != null) {
            node.putObject("Duration").apply {
                put("Value", duration)
                put("Error", durationError)
            }
        }
        if (specialistError != null) {
            if (specialist != null) {
                node.putObject("Specialist").apply {
                    put("Value", specialist)
                    put("Error", specialistError)
                }
            } else {
                node.putObject("Specialist").apply {
                    putNull("Value")
                    put("Error", specialistError)
                }
            }
        }
        if (launderetteError != null) {
            node.putObject("Launderette").apply {
                put("Value", launderette)
                put("Error", launderetteError)
            }
        }
        return node
    }
}
