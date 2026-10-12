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
import org.http4k.core.Status.Companion.CREATED
import org.http4k.core.with
import org.http4k.format.Jackson
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.data.json.AddMachine
import ru.yarsu.operations.CreateMachineOperationImpl
import ru.yarsu.operations.GetJsonMachineFieldsOperationImpl
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.util.UUID
import java.util.UUID.fromString

class AddNewMachineHandler(
    private val jsonFactory: JsonNodeFactory,
    private val fields: GetJsonMachineFieldsOperationImpl,
    private val newMachine: CreateMachineOperationImpl,
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
            val launderetteId = if (launderette != null) fromString(launderette) else null
            val newId =
                newMachine.createMachine(
                    title ?: "",
                    type ?: "",
                    parse(startDateTime ?: "", ISO_LOCAL_DATE_TIME),
                    condition?.toBooleanStrict() ?: true,
                    capacity?.toDouble() ?: 0.0,
                    price?.toInt() ?: 0,
                    duration?.toInt() ?: 0,
                    fromString(specialist),
                    launderetteId,
                )
            return Response(CREATED).with(
                jsonBodyLens of correct(newId),
            )
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

    private fun correct(id: UUID): JsonNode =
        json {
            obj(
                "Id" to string(id.toString()),
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
            if (title != null) {
                node.putObject("Title").apply {
                    put("Value", title)
                    put("Error", titleError)
                }
            } else {
                node.putObject("Title").apply {
                    putNull("Value")
                    put("Error", titleError)
                }
            }
        }
        if (typeError != null) {
            if (type != null) {
                node.putObject("Type").apply {
                    put("Value", type)
                    put("Error", typeError)
                }
            } else {
                node.putObject("Type").apply {
                    putNull("Value")
                    put("Error", typeError)
                }
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
            if (capacity != null) {
                node.putObject("Capacity").apply {
                    put("Value", capacity)
                    put("Error", capacityError)
                }
            } else {
                node.putObject("Capacity").apply {
                    putNull("Value")
                    put("Error", capacityError)
                }
            }
        }
        if (priceError != null) {
            if (price != null) {
                node.putObject("Price").apply {
                    put("Value", price)
                    put("Error", priceError)
                }
            } else {
                node.putObject("Price").apply {
                    putNull("Value")
                    put("Error", priceError)
                }
            }
        }
        if (durationError != null) {
            if (duration != null) {
                node.putObject("Duration").apply {
                    put("Value", duration)
                    put("Error", durationError)
                }
            } else {
                node.putObject("Duration").apply {
                    putNull("Value")
                    put("Error", durationError)
                }
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
