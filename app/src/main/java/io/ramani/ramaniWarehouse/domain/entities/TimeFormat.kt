package io.ramani.ramaniWarehouse.domain.entities

import io.ramani.ramaniWarehouse.domainCore.entities.IBuilder

/**
 * Created by Amr on 12/4/17.
 */
data class TimeFormat(val name: String = "",
                      val format: String = "") {

    override fun toString(): String =
            name

    class Builder : IBuilder<TimeFormat> {
        private var name = ""
        private var format = ""

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun format(format: String): Builder {
            this.format = format
            return this
        }

        override fun build(): TimeFormat =
                TimeFormat(name, format)
    }

    class TestBuilder {
        companion object {
            fun buildTimeFormat() =
                    Builder().name("24 hours format")
                            .format("HH")
                            .build()
        }
    }
}