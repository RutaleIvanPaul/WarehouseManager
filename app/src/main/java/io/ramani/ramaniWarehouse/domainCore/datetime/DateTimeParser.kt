package io.ramani.ramaniWarehouse.domainCore.datetime

/**
 * Created by Amr on 12/15/17.
 */
interface DateTimeParser {
    fun convertToServerDate(date: Long): String?

    fun convertToServerDashedDate(date: Long): String?

    fun convertFromServerDate(date: String?): Long?

    fun convertFromServerTime(time: String?): Long?

    fun convertFromServerTimeOnly(time: String?): Long?

    fun convertFromServer(dateTime: String?, format: String): Long?

    fun isToday(date: String): Boolean

    fun isFutureDate(date: String): Boolean

    object DateTimeFormats {
        const val DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss"

        const val DATE_FORMAT = "dd/MM/yyyy"

        const val TIME_FORMAT = "HH:mm:ss"

        const val TIME_HOURS_AND_MINUTES_FORMAT = "HH:mm"
    }


}