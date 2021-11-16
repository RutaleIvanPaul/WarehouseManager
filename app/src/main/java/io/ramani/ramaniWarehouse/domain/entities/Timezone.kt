package io.ramani.ramaniWarehouse.domain.entities

import io.ramani.ramaniWarehouse.core.domain.entities.IBuilder

/**
 * Created by Amr on 11/28/17.
 */
data class Timezone(val key: String = "",
                    val value: String = "") {
    class Builder : IBuilder<Timezone> {
        private var key = ""
        private var value = ""

        fun key(key: String): Builder {
            this.key = key
            return this
        }

        fun value(value: String): Builder {
            this.value = value
            return this
        }

        override fun build(): Timezone =
                Timezone(key, value)
    }

    class TestBuilder {
        companion object {
            fun buildNormalTimezone() =
                    Builder().key("UTC")
                            .value("(UTC -00:00) UTC")
                            .build()

            fun buildList() =
                    listOf(buildNormalTimezone())
        }
    }
}