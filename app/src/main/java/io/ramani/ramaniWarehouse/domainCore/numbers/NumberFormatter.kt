package io.ramani.ramaniWarehouse.core.domain.numbers

interface NumberFormatter {
    fun format(num: Double): CharSequence?
}