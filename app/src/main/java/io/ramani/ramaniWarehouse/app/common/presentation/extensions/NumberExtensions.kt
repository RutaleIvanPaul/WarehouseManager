package io.ramani.ramaniWarehouse.app.common.presentation.extensions

import java.util.*

fun Int.greaterThan(numberToCompare: Int) =
        this > numberToCompare

fun Int.greaterThanOrEqual(numberToCompare: Int) =
        this >= numberToCompare

fun Int.lessThan(numberToCompare: Int) =
        this < numberToCompare

fun Double.format(digits: Int): String {
    val locale = Locale.getDefault()

    return if (locale == Locale("ar")) {
        String.format(Locale("ar"), "%,.${digits}f", this)
    } else {
        String.format(Locale.US, "%,.${digits}f", this)
    }
}

fun Double.roundWithFormat(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()