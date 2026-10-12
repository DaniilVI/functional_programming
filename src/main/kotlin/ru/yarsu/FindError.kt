package ru.yarsu

import com.fasterxml.jackson.databind.JsonNode
import org.http4k.core.Request
import org.http4k.format.Jackson
import org.http4k.lens.LensFailure

object FindError {
    private val json = Jackson
    private val baseErrors =
        mapOf<String, String>(
            "path 'user-id-from' must be string" to "Некорректное значение переданного параметра id." +
                " Ожидается UUID, но получено текстовое значение ",
            "path 'user-id-from' is required" to "Отсутствие обязательного параметра id",
            "path 'machine-id' must be string" to "Некорректное значение переданного параметра id." +
                " Ожидается UUID, но получено текстовое значение ",
            "path 'machine-id' is required" to "Отсутствие обязательного параметра id",
            "path 'launderette-id' must be string" to "Некорректное значение переданного параметра id." +
                " Ожидается UUID, но получено текстовое значение ",
            "path 'launderette-id' is required" to "Отсутствие обязательного параметра id",
            "path 'user-id-to' must be string" to "Некорректное значение переданного параметра id." +
                " Ожидается UUID, но получено текстовое значение ",
            "path 'user-id-to' is required" to "Отсутствие обязательного параметра id",
            "query 'page' must be integer" to "Некорректное значение переданного параметра page." +
                " Ожидается целое число, но получено значение ",
            "query 'records-per-page' must be integer" to "Некорректное значение переданного параметра records-per-page." +
                " Ожидается целое число, но получено значение ",
            "query 'weight' must be number" to "Некорректное значение переданного параметра weight." +
                " Ожидается положительное число, но получено значение ",
            "query 'weight' is required" to "Отсутствие обязательного параметра weight",
            "query 'budget' must be integer" to "Некорректное значение переданного параметра budget." +
                " Ожидается натуральное число, но получено значение ",
            "query 'budget' is required" to "Отсутствие обязательного параметра budget",
            "query 'now' is required" to "Отсутствие обязательного параметра now",
            "query 'now' must be string" to "Некорректное значение переданного параметра now." +
                " Ожидается дата и время, но получено значение ",
        )

    private fun getError(error: LensFailure): String {
        val errorMessage = error.message.toString()
        var answer = baseErrors.getOrDefault(errorMessage, errorMessage)
        if (errorMessage.contains("must be") && answer != errorMessage) {
            answer +=
                if (errorMessage.contains("path")) {
                    if (errorMessage.contains("user-id-from") ||
                        errorMessage.contains("launderette-id") &&
                        error.target.toString().contains("GET")
                    ) {
                        getIncorrectFromString(error.target.toString())
                    } else {
                        getIncorrectPathString(error.target.toString())
                    }
                } else {
                    getIncorrectQueryString(error.target.toString(), errorMessage)
                }
        }
        return answer
    }

    private fun getIncorrectFromString(errorString: String): String = "«" + errorString.split(" ")[1].split("/")[3] + "»"

    private fun getIncorrectPathString(errorString: String): String = "«" + errorString.split(" ")[1].substringAfterLast("/") + "»"

    private fun getIncorrectQueryString(
        errorString: String,
        message: String,
    ): String {
        val parametersString = errorString.split(" ")[1].substringAfterLast("/")
        val dataString = parametersString.split(regex = Regex("[?&]"))
        var resultString = ""
        val argument = message.split(" ")[1].replace("\'", "")
        dataString.forEach {
            if (it.substringBefore("=") == argument) {
                resultString += it.substringAfterLast("=")
            }
        }
        return "«$resultString»"
    }

    fun printError(error: LensFailure): JsonNode =
        json {
            obj(
                "Error" to string(getError(error)),
            )
        }

    fun printBadJson(request: Request): JsonNode =
        json {
            obj(
                "Value" to string(request.bodyString()),
                "Error" to string("The request body is not a json object"),
            )
        }
}
