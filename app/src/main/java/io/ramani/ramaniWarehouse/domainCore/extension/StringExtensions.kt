package io.ramani.ramaniWarehouse.domainCore.extension

import io.ramani.ramaniWarehouse.domainCore.datetime.FromServerDateTimeParser


fun String.toBoolean() =
        when (this) {
            "1", "true" -> true
            else -> false
        }

fun String.takeIfNotBlank() =
        takeIf { it.isNotBlank() }

fun String.valueOrNull() =
        when (this) {
            "" -> null
            else -> this
        }

fun String.equalsAnyOf(list: List<String>) =
        list.contains(this)

fun String?.convertFromServerDate(parser: FromServerDateTimeParser, defaultValue: Long = 0) =
        parser.convertFromServerDate(this ?: "") ?: defaultValue

fun String?.convertFromServerTime(parser: FromServerDateTimeParser, defaultValue: Long = 0) =
        parser.convertFromServerTime(this ?: "") ?: defaultValue
