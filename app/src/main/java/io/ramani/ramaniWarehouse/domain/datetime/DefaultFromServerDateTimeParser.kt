package io.ramani.ramaniWarehouse.domain.datetime


import io.ramani.ramaniWarehouse.domainCore.datetime.FromServerDateTimeParser
import io.ramani.ramaniWarehouse.domainCore.log.logError
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatterBuilder

class DefaultFromServerDateTimeParser : FromServerDateTimeParser {
    companion object {
        private const val SERVER_RECEIVE_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"
        private const val SERVER_RECEIVE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
    }

    override fun convertFromServerDate(date: String): Long? =
        parseDate(date, SERVER_RECEIVE_DATE_FORMAT)

    override fun convertFromServerTime(time: String): Long? =
        parseDate(time, SERVER_RECEIVE_TIME_FORMAT)

    override fun convertFromServer(dateTime: String, format: String): Long? =
        parseDate(dateTime, format)

    private fun parseDate(date: String, pattern: String): Long? {
        if (date.isBlank()) {
            return -1L
        }
        return try {
            DateTime.parse(date, dateTimeFormatter(pattern)).millis
        } catch (ex: Exception) {
            logError("Error parsing date $date", ex)
            -1L
        }
    }

    private fun dateTimeFormatter(pattern: String) =
        DateTimeFormatterBuilder().appendPattern(pattern).toFormatter()
}