package io.ramani.ramaniWarehouse.core.domain.date

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Interval
import org.joda.time.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Amr on 10/28/17.
 */
object DateHelper {

}

fun now() = Calendar.getInstance().timeInMillis

fun nowDate() = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

fun Int.getMilliSecondsFromMinutes(): Long =
        (this * 60 * 1000).toLong()

fun Double.getMilliSecondsFromHours(): Long =
        (this * 60 * 60 * 1000).toLong()

fun String.toDate(format: String): Date {
    return try {
        SimpleDateFormat(format, Locale.ENGLISH).parse(this)?: Date()
    } catch (e: ParseException) {
        Date(now())
    }
}

fun Long.formatDate(format: String, locale: Locale = Locale.getDefault()): String {
    val sdf = SimpleDateFormat(format, locale)
    return sdf.format(this)
}

fun Long.toCalendar() = Calendar.getInstance().apply { timeInMillis = this@toCalendar }

fun Long.isBeforeNow(): Boolean {
    val today = nowDate()

    val date = Calendar.getInstance().apply {
        timeInMillis = this@isBeforeNow
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

    }.timeInMillis

    return date < today
}

fun Long.isToday() =
        isEqualDate(nowDate())

fun Long.isAfterDate(date: Long) =
        this > date

fun Long.isEqualDate(date: Long) =
        this == date

fun Long.isBeforeDate(date: Long) =
        this < date

fun Long.getHour(): Int {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Long.getMinute(): Int {
    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return calendar.get(Calendar.MINUTE)
}

val Long.hourOfDay: Int
    get() = DateTime.now().withMillis(this).hourOfDay

val Long.minuteOfHour: Int
    get() = DateTime.now().withMillis(this).minuteOfHour

fun Long.withUTCDate(): Long =
        DateTime.now().withMillis(this).let {
            DateTime.now(DateTimeZone.UTC).withTime(it.hourOfDay, it.minuteOfHour, it.secondOfMinute, it.millisOfSecond).millis
        }

fun Long.mergeWithDate(date: Long): Long =
        DateTime.now().withMillis(date).let { dateTmp ->
            val timeTmp = DateTime.now().withMillis(this)
            DateTime.now().withDate(dateTmp.year, dateTmp.monthOfYear, dateTmp.dayOfMonth)
                    .withTime(timeTmp.hourOfDay, timeTmp.minuteOfHour, timeTmp.secondOfMinute, timeTmp.millisOfSecond)
                    .millis
        }

fun Long.periodDisplay(): PeriodDisplay {
    val now = DateTime.now()

    val time = DateTime.now().withMillis(this)

    val interval = Interval(time, now)

    interval.toPeriod().years

    val period = interval.toPeriod()
    return when {
        period.years > 0 -> PeriodDisplay(period.years, PeriodDisplay.YEARS)
        period.months > 0 -> PeriodDisplay(period.months, PeriodDisplay.MONTHS)
        period.weeks > 0 -> PeriodDisplay(period.weeks, PeriodDisplay.WEEKS)
        period.days > 0 -> PeriodDisplay(period.days, PeriodDisplay.DAYS)
        period.hours > 0 -> PeriodDisplay(period.hours, PeriodDisplay.HOURS)
        period.minutes > 0 -> PeriodDisplay(period.minutes, PeriodDisplay.MINUTES)
        period.seconds > 0 -> PeriodDisplay(period.seconds, PeriodDisplay.SECONDS)
        else -> PeriodDisplay(-1, -1)
    }
}

fun currentDate(): DateTime = DateTime.now()

fun DateTime.getDateInPast(monthDifference: Int) =
        this.minusMonths(monthDifference).millis

fun today(): String = LocalDate().toString("dd/MM/YYYY")

data class PeriodDisplay(val period: Int, val type: Int) {
    companion object {
        const val YEARS = 0
        const val MONTHS = 1
        const val WEEKS = 2
        const val DAYS = 3
        const val HOURS = 4
        const val MINUTES = 5
        const val SECONDS = 7
    }
}




