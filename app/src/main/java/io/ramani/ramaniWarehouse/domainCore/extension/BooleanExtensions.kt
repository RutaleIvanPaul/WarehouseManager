package io.ramani.ramaniWarehouse.core.domain.extension

fun Boolean.toReplyText() =
        when (this) {
            true -> {
                "Yes"
            }
            false -> {
                "No"
            }
        }