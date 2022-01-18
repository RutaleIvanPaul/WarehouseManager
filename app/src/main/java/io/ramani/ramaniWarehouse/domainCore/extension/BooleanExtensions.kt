package io.ramani.ramaniWarehouse.domainCore.extension

fun Boolean.toReplyText() =
        when (this) {
            true -> {
                "Yes"
            }
            false -> {
                "No"
            }
        }