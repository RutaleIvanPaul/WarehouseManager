package io.ramani.ramaniWarehouse.domain.datetime

import android.annotation.SuppressLint
import io.ramani.ramaniWarehouse.domainCore.datetime.DateTimeParser
import io.ramani.ramaniWarehouse.domainCore.datetime.IDateTimeManager
import io.ramani.ramaniWarehouse.domainCore.log.logError

import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatterBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Amr on 12/15/17.
 */
class DefaultDateTimeParser(private val dateTimeManager: IDateTimeManager) : DateTimeParser {

    companion object {
        private const val SERVER_SEND_DATE_FORMAT = "dd/MM/yyyy"
        private const val SERVER_SEND_TIME_FORMAT = "HH:mm:ss"
        private const val SERVER_SEND_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
        private const val SERVER_SEND_TIME_NO_SECONDS_FORMATS = "HH:mm"
        private const val SERVER_RECEIVE_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"
        private const val SERVER_RECEIVE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"
        const val DATE_DASHED_FORMAT = "yyyy-MM-dd"
    }

    override fun convertToServerDate(date: Long): String? = formatToDateString(date, SERVER_SEND_DATE_FORMAT)
    override fun convertToServerDashedDate(date: Long): String?  = formatToDateString(date, DATE_DASHED_FORMAT)

    override fun convertFromServerDate(date: String?): Long? {
        var parsedDate = parseDate(date, SERVER_RECEIVE_DATE_FORMAT)
        if (parsedDate == -1L) {
            parsedDate = parseDate(date, SERVER_SEND_DATE_FORMAT)
        }
        return parsedDate
    }


    override fun convertFromServerTime(time: String?): Long? =
            parseDate(time, SERVER_RECEIVE_TIME_FORMAT)

    override fun convertFromServerTimeOnly(time: String?): Long? =
            parseDate(time, SERVER_SEND_TIME_FORMAT)

    override fun convertFromServer(dateTime: String?, format: String): Long? =
            parseDate(dateTime, format)

    @SuppressLint("SimpleDateFormat")
    private fun formatToDateString(date: Long, format: String): String? {
        return try {
            val sdf = SimpleDateFormat(format, Locale.ENGLISH)
            sdf.format(date)
        } catch (ex: Exception) {
            logError("Error formatting date $date, $format", ex)
            null
        }
    }



    private fun parseDate(date: String?, pattern: String): Long? {
        if (date.isNullOrBlank()) {
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

    override fun isToday(date: String): Boolean {
        val formatter = DateTimeFormat.forPattern(SERVER_SEND_DATE_FORMAT)
        val localDate = formatter.parseLocalDate(date)
        return localDate.isEqual(LocalDate())
    }

    override fun isFutureDate(date: String): Boolean {
        val formatter = DateTimeFormat.forPattern(SERVER_SEND_DATE_FORMAT)
        val localDate = formatter.parseLocalDate(date)
        return localDate.isAfter(LocalDate())
    }
}