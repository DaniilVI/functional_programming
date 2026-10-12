package ru.yarsu

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.core.io.JsonEOFException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.http4k.core.ContentType
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.with
import org.http4k.lens.LensFailure
import org.http4k.lens.contentType
import ru.yarsu.FindError.printBadJson
import ru.yarsu.FindError.printError
import ru.yarsu.Lens.jsonBodyLens
import ru.yarsu.Logger.logger

object Filter {
    fun jsonContentTypeFilter(): Filter =
        Filter { next: HttpHandler ->
            { request ->
                val response = next(request)
                if (response.body.length?.toInt() != 0) {
                    response.contentType(ContentType.APPLICATION_JSON)
                } else {
                    response
                }
            }
        }

    fun errorFilter(): Filter =
        Filter { next: HttpHandler ->
            { request: Request ->
                try {
                    next(request)
                } catch (lensFailure: LensFailure) {
                    Response(Status.BAD_REQUEST).with(
                        jsonBodyLens of printError(lensFailure),
                    )
                } catch (e: JsonEOFException) {
                    Response(Status.BAD_REQUEST).with(
                        jsonBodyLens of printBadJson(request),
                    )
                } catch (e: JsonParseException) {
                    Response(Status.BAD_REQUEST).with(
                        jsonBodyLens of printBadJson(request),
                    )
                } catch (e: MismatchedInputException) {
                    Response(Status.BAD_REQUEST).with(
                        jsonBodyLens of printBadJson(request),
                    )
                } catch (e: Exception) {
                    Response(Status.BAD_REQUEST)
                        .body("{\"error\":\"${e.message}\"}")
                }
            }
        }

    fun loggingFilter(): Filter =
        Filter { next: HttpHandler ->
            { request: Request ->
                val start = System.currentTimeMillis()
                val response = next(request)
                val latency = System.currentTimeMillis() - start
                logger
                    .atInfo()
                    .setMessage("Request")
                    .addKeyValue("METHOD", request.method)
                    .addKeyValue("URI", request.uri)
                    .addKeyValue("ADDRESS", request.source)
                    .log()
                logger
                    .atInfo()
                    .setMessage("Response")
                    .addKeyValue("STATUS", response.status)
                    .addKeyValue("LATENCY", latency)
                    .log()
                response
            }
        }
}
