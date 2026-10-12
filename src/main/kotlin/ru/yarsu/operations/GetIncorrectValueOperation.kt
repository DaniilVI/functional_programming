package ru.yarsu.operations

import org.http4k.lens.WebForm

interface GetIncorrectValueOperation {
    fun get(form: WebForm): Map<String, String?>
}

class GetIncorrectValueOperationImpl : GetIncorrectValueOperation {
    override fun get(form: WebForm): Map<String, String?> {
        val errorMap = mutableMapOf<String, String?>()
        form.errors.forEach {
            val parametr = it.meta.name
            val value = form.fields[parametr]?.first() ?: ""
            errorMap[parametr] = value
        }
        return errorMap
    }
}
