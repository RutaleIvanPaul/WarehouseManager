package io.ramani.ramaniWarehouse.domainCore.numbers

interface NumberFormatter {
    fun format(num: Double): CharSequence?
}