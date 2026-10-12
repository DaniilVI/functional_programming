package ru.yarsu

import org.http4k.core.Body
import org.http4k.format.Jackson.auto
import org.http4k.lens.FormField
import org.http4k.lens.LensFailure
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.Validator
import org.http4k.lens.composite
import org.http4k.lens.dateTime
import org.http4k.lens.double
import org.http4k.lens.int
import org.http4k.lens.string
import org.http4k.lens.uuid
import org.http4k.lens.webForm
import java.time.LocalDateTime
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

object Lens {
    val pageDataLens =
        Query
            .int()
            .map(
                { number ->
                    if (number < 1) {
                        throw LensFailure(
                            message =
                                "Некорректное значение параметра page." +
                                    " Ожидается натуральное число, но получено $number",
                        )
                    }
                    number
                },
                { number -> number },
            ).defaulted("page", 1)

    val countDataLens =
        Query
            .int()
            .map(
                { number ->
                    if (number != 5 && number != 10 && number != 20 && number != 50) {
                        throw LensFailure(
                            message =
                                "Некорректное значение параметра records-per-page." +
                                    " Ожидается 5, 10, 20 или 50, но получено $number",
                        )
                    }
                    number
                },
                { number -> number },
            ).defaulted("records-per-page", 10)

    val pageableLens =
        Query.composite { request ->
            PaginationParameters(
                pageDataLens(request),
                countDataLens(request),
            )
        }

    val machineIdLens = Path.uuid().of("machine-id")
    val launderetteIdLens = Path.uuid().of("launderette-id")
    val weightLens =
        Query
            .double()
            .map(
                { number ->
                    if (number <= 0) {
                        throw LensFailure(
                            message =
                                "Некорректное значение параметра weight." +
                                    " Ожидается положительное число, но получено $number",
                        )
                    }
                    number
                },
                { number -> number },
            ).required("weight")
    val budgetLens =
        Query
            .int()
            .map(
                { number ->
                    if (number <= 0) {
                        throw LensFailure(
                            message =
                                "Некорректное значение параметра budget." +
                                    " Ожидается натуральное число, но получено $number",
                        )
                    }
                    number
                },
                { number -> number },
            ).required("budget")

    val timeNowLens = Query.dateTime(ISO_LOCAL_DATE_TIME).required("now")

    val nameField = FormField.string().required("Name")
    val emailField = FormField.string().required("Email")
    val registrationDateTimeField =
        FormField.dateTime(ISO_LOCAL_DATE_TIME).defaulted(
            "RegistrationDateTime",
            parse(LocalDateTime.now().toString(), ISO_LOCAL_DATE_TIME),
        )
    val addUserLens =
        Body
            .webForm(
                Validator.Feedback,
                nameField,
                emailField,
                registrationDateTimeField,
            ).toLens()

    val userIdFromLens = Path.uuid().of("user-id-from")
    val userIdToLens = Path.uuid().of("user-id-to")

    val jsonBodyLens = Body.auto<Any>().toLens()
}
