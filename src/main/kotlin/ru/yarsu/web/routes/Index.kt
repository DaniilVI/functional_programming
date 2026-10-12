package ru.yarsu.web.routes

import org.http4k.core.ContentType
import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status.Companion.OK
import org.http4k.lens.contentType

class Index : HttpHandler {
    override fun invoke(request: Request): Response =
        Response(OK)
            .contentType(ContentType.TEXT_HTML)
            .body("Приложение, решающее задачу по управлению списком с данными")
}
