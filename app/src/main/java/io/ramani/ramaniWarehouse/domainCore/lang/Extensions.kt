package io.ramani.ramaniWarehouse.domainCore.lang

import android.webkit.URLUtil
import java.util.*

/**
 * Created by Amr on 11/25/17.
 */
fun Int.toBoolean() = this > 0

fun Boolean.toInt() = if (this) 1 else 0

fun <T> List<T>.toCommaSeparated() = foldIndexed("") { index, acc, item ->
    acc + if (index < size - 1) {
        "$item, "
    } else {
        item.toString()
    }
}

fun <T> List<T>.toSpaceSeparated() = foldIndexed("") { index, acc, item ->
    acc + if (index < size - 1) {
        "$item "
    } else {
        item.toString()
    }
}

fun <T> List<T>.toDashSeparated() = foldIndexed("") { index, acc, item ->
    acc + if (index < size - 1) {
        "$item - "
    } else {
        item.toString()
    }
}

fun <T> List<T>.toWithoutSpaceCommaSeparated() = foldIndexed("") { index, acc, item ->
    acc + if (index < size - 1) {
        "$item,"
    } else {
        item.toString()
    }
}

fun <T> List<T>.toLineSeparated() = foldIndexed("") { index, acc, item ->
    acc + if (index < size - 1) {
        "$item\n"
    } else {
        item.toString()
    }
}

fun <T> List<T>.isIndexInBounds(index: Int) = index in 0..(size - 1)

fun String.isUrl(): Boolean =
        URLUtil.isValidUrl(this)


fun String.mapRemoteStringToBoolean(): Boolean =
        when (this) {
            "1" -> true
            else -> false
        }

fun Boolean.mapRemoteBooleanToString(): String =
        when (this) {
            false -> "0"
            true -> "1"
        }

fun String.getLocaleFromString(): Locale {
    val localeParts = this.lowercase(Locale.getDefault()).split("-")
    return if (localeParts.size > 1) {
        Locale(localeParts[0], localeParts[1])
    } else {
        Locale(localeParts[0])
    }
}

fun Any?.isNotNull() = this != null