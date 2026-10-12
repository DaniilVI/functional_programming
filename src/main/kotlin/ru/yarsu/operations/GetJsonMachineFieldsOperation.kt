package ru.yarsu.operations

import ru.yarsu.data.LaunderetteStorage
import ru.yarsu.data.Type
import ru.yarsu.data.UserStorage
import java.time.LocalDate
import java.time.LocalDateTime.parse
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.format.DateTimeParseException
import java.util.UUID.fromString

interface GetJsonMachineFieldsOperation {
    fun title(data: String?): MutableList<String?>

    fun type(data: String?): MutableList<String?>

    fun startDateTime(data: String?): MutableList<String?>

    fun condition(data: String?): MutableList<String?>

    fun capacity(data: String?): MutableList<String?>

    fun price(data: String?): MutableList<String?>

    fun duration(data: String?): MutableList<String?>

    fun specialist(data: String?): MutableList<String?>

    fun launderette(data: String?): MutableList<String?>
}

class GetJsonMachineFieldsOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
    private val userStorage: UserStorage,
) : GetJsonMachineFieldsOperation {
    override fun title(data: String?): MutableList<String?> {
        val title: String?
        val titleError: String?
        if (data != null) {
            if (data.startsWith("[") && data.endsWith("]")) {
                title = data
                titleError = "Ожидается строка"
            } else {
                title = data
                titleError = null
            }
        } else {
            title = null
            titleError = "Поле отсутствует"
        }
        return mutableListOf(title, titleError)
    }

    override fun type(data: String?): MutableList<String?> {
        val type: String?
        val typeError: String?
        if (data != null) {
            type = data
            val listType = mutableListOf<String>()
            Type.entries.forEach {
                listType.add(it.russianName)
            }
            typeError =
                if (listType.contains(type)) {
                    null
                } else {
                    "Ожидается корректный тип оборудования"
                }
        } else {
            type = null
            typeError = "Поле отсутствует"
        }
        return mutableListOf(type, typeError)
    }

    override fun startDateTime(data: String?): MutableList<String?> {
        var startDateTime: String?
        val startDateTimeError: String?
        if (data != null) {
            startDateTime =
                try {
                    parse(data, ISO_LOCAL_DATE_TIME).toString()
                } catch (e: DateTimeParseException) {
                    null
                }
            if (startDateTime == null) {
                startDateTime = data
                startDateTimeError = "Ожидается корректные дата и время"
            } else {
                startDateTimeError = null
            }
        } else {
            startDateTime = LocalDate.now().toString() + "T00:00:00"
            startDateTimeError = null
        }
        return mutableListOf(startDateTime, startDateTimeError)
    }

    override fun condition(data: String?): MutableList<String?> {
        var condition: String?
        val conditionError: String?
        if (data != null) {
            condition =
                try {
                    data.toBooleanStrict().toString()
                } catch (e: IllegalArgumentException) {
                    null
                }
            if (condition == null) {
                condition = data
                conditionError = "Ожидается логическое значение"
            } else {
                conditionError = null
            }
        } else {
            condition = true.toString()
            conditionError = null
        }

        return mutableListOf(condition, conditionError)
    }

    override fun capacity(data: String?): MutableList<String?> {
        var capacity: String?
        val capacityError: String?
        if (data != null) {
            capacity =
                try {
                    data.toDouble().toString()
                } catch (e: NumberFormatException) {
                    null
                }
            if (capacity == null) {
                capacity = data
                capacityError = "Ожидается положительное число"
            } else {
                capacityError =
                    if (capacity.toDouble() > 0) {
                        null
                    } else {
                        "Ожидается положительное число"
                    }
            }
        } else {
            capacity = null
            capacityError = "Поле отсутствует"
        }
        return mutableListOf(capacity, capacityError)
    }

    override fun price(data: String?): MutableList<String?> {
        var price: String?
        val priceError: String?
        if (data != null) {
            price =
                try {
                    data.toInt().toString()
                } catch (e: NumberFormatException) {
                    null
                }
            if (price == null) {
                price = data
                priceError = "Ожидается целое неотрицательное значение"
            } else {
                priceError =
                    if (price.toInt() >= 0) {
                        null
                    } else {
                        "Ожидается целое неотрицательное значение"
                    }
            }
        } else {
            price = null
            priceError = "Поле отсутствует"
        }
        return mutableListOf(price, priceError)
    }

    override fun duration(data: String?): MutableList<String?> {
        var duration: String?
        val durationError: String?
        if (data != null) {
            duration =
                try {
                    data.toInt().toString()
                } catch (e: NumberFormatException) {
                    null
                }
            if (duration == null) {
                duration = data
                durationError = "Ожидается целое положительное значение"
            } else {
                durationError =
                    if (duration.toInt() > 0) {
                        null
                    } else {
                        "Ожидается целое положительное значение"
                    }
            }
        } else {
            duration = null
            durationError = "Поле отсутствует"
        }
        return mutableListOf(duration, durationError)
    }

    override fun specialist(data: String?): MutableList<String?> {
        var specialist: String?
        val specialistError: String?
        if (data != null) {
            specialist =
                try {
                    fromString(data).toString()
                } catch (e: IllegalArgumentException) {
                    null
                }
            if (specialist == null) {
                specialist = data
                specialistError = "Ожидается корректный UUID"
            } else {
                specialistError =
                    if (!userStorage.getAll().containsKey(fromString(specialist))) {
                        "Работник с таким UUID не найден"
                    } else {
                        null
                    }
            }
        } else {
            specialist = null
            specialistError = "Поле отсутствует"
        }
        return mutableListOf(specialist, specialistError)
    }

    override fun launderette(data: String?): MutableList<String?> {
        var launderette: String?
        val launderetteError: String?
        if (data != null) {
            launderette =
                try {
                    fromString(data).toString()
                } catch (e: IllegalArgumentException) {
                    null
                }
            if (launderette == null) {
                launderette = data
                launderetteError = "Ожидается корректный UUID"
            } else {
                launderetteError =
                    if (!launderetteStorage.getAll().containsKey(fromString(launderette))) {
                        "Прачечная с таким UUID не найден"
                    } else {
                        null
                    }
            }
        } else {
            launderette = null
            launderetteError = null
        }
        return mutableListOf(launderette, launderetteError)
    }
}

class GetJsonEditMachineFieldsOperationImpl(
    private val launderetteStorage: LaunderetteStorage,
    private val userStorage: UserStorage,
) : GetJsonMachineFieldsOperation {
    override fun title(data: String?): MutableList<String?> {
        val title: String?
        val titleError: String?
        if (data != null) {
            if (data.startsWith("[") && data.endsWith("]")) {
                title = data
                titleError = "Ожидается строка"
            } else {
                title = data
                titleError = null
            }
        } else {
            title = null
            titleError = null
        }
        return mutableListOf(title, titleError)
    }

    override fun type(data: String?): MutableList<String?> {
        val type: String?
        val typeError: String?
        if (data != null) {
            type = data
            val listType = mutableListOf<String>()
            Type.entries.forEach {
                listType.add(it.russianName)
            }
            typeError =
                if (listType.contains(type)) {
                    null
                } else {
                    "Ожидается корректный тип оборудования"
                }
        } else {
            type = null
            typeError = null
        }
        return mutableListOf(type, typeError)
    }

    override fun startDateTime(data: String?): MutableList<String?> {
        var startDateTime: String?
        val startDateTimeError: String?
        if (data != null) {
            startDateTime =
                try {
                    parse(data, ISO_LOCAL_DATE_TIME).toString()
                } catch (e: DateTimeParseException) {
                    null
                }
            if (startDateTime == null) {
                startDateTime = data
                startDateTimeError = "Ожидается корректные дата и время"
            } else {
                startDateTimeError = null
            }
        } else {
            startDateTime = null
            startDateTimeError = null
        }
        return mutableListOf(startDateTime, startDateTimeError)
    }

    override fun condition(data: String?): MutableList<String?> {
        var condition: String?
        val conditionError: String?
        if (data != null) {
            condition =
                try {
                    data.toBooleanStrict().toString()
                } catch (e: IllegalArgumentException) {
                    null
                }
            if (condition == null) {
                condition = data
                conditionError = "Ожидается логическое значение"
            } else {
                conditionError = null
            }
        } else {
            condition = null
            conditionError = null
        }

        return mutableListOf(condition, conditionError)
    }

    override fun capacity(data: String?): MutableList<String?> {
        var capacity: String?
        val capacityError: String?
        if (data != null) {
            capacity =
                try {
                    data.toDouble().toString()
                } catch (e: NumberFormatException) {
                    null
                }
            if (capacity == null) {
                capacity = data
                capacityError = "Ожидается положительное число"
            } else {
                capacityError =
                    if (capacity.toDouble() > 0) {
                        null
                    } else {
                        "Ожидается положительное число"
                    }
            }
        } else {
            capacity = null
            capacityError = null
        }
        return mutableListOf(capacity, capacityError)
    }

    override fun price(data: String?): MutableList<String?> {
        var price: String?
        val priceError: String?
        if (data != null) {
            price =
                try {
                    data.toInt().toString()
                } catch (e: NumberFormatException) {
                    null
                }
            if (price == null) {
                price = data
                priceError = "Ожидается целое неотрицательное значение"
            } else {
                priceError =
                    if (price.toInt() >= 0) {
                        null
                    } else {
                        "Ожидается целое неотрицательное значение"
                    }
            }
        } else {
            price = null
            priceError = null
        }
        return mutableListOf(price, priceError)
    }

    override fun duration(data: String?): MutableList<String?> {
        var duration: String?
        val durationError: String?
        if (data != null) {
            duration =
                try {
                    data.toInt().toString()
                } catch (e: NumberFormatException) {
                    null
                }
            if (duration == null) {
                duration = data
                durationError = "Ожидается целое положительное значение"
            } else {
                durationError =
                    if (duration.toInt() > 0) {
                        null
                    } else {
                        "Ожидается целое положительное значение"
                    }
            }
        } else {
            duration = null
            durationError = null
        }
        return mutableListOf(duration, durationError)
    }

    override fun specialist(data: String?): MutableList<String?> {
        var specialist: String?
        val specialistError: String?
        if (data != null) {
            specialist =
                try {
                    fromString(data).toString()
                } catch (e: IllegalArgumentException) {
                    null
                }
            if (specialist == null) {
                specialist = data
                specialistError = "Ожидается корректный UUID"
            } else {
                specialistError =
                    if (!userStorage.getAll().containsKey(fromString(specialist))) {
                        "Работник с таким UUID не найден"
                    } else {
                        null
                    }
            }
        } else {
            specialist = null
            specialistError = "Поле отсутствует"
        }
        return mutableListOf(specialist, specialistError)
    }

    override fun launderette(data: String?): MutableList<String?> {
        var launderette: String?
        val launderetteError: String?
        if (data != null) {
            launderette =
                try {
                    fromString(data).toString()
                } catch (e: IllegalArgumentException) {
                    null
                }
            if (launderette == null) {
                launderette = data
                launderetteError = "Ожидается корректный UUID"
            } else {
                launderetteError =
                    if (!launderetteStorage.getAll().containsKey(fromString(launderette))) {
                        "Прачечная с таким UUID не найден"
                    } else {
                        null
                    }
            }
        } else {
            launderette = null
            launderetteError = null
        }
        return mutableListOf(launderette, launderetteError)
    }
}
