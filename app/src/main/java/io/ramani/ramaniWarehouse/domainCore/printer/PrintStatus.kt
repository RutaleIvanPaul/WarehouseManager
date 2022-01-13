package io.ramani.ramaniWarehouse.domainCore.printer

data class PrintStatus(
    var status: Boolean = false,
    var error: String = ""
)
