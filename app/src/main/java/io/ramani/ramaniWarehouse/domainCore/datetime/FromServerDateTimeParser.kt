package io.ramani.ramaniWarehouse.domainCore.datetime

interface FromServerDateTimeParser {
    fun convertFromServerDate(date: String): Long?

    fun convertFromServerTime(time: String): Long?

    fun convertFromServer(dateTime: String, format: String): Long?
}