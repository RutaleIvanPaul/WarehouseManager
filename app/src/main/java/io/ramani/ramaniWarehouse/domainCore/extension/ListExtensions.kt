package io.ramani.ramaniWarehouse.core.domain.extension

inline fun <reified T> MutableList<T>.removeItem(item: T) =
        if (this.contains(item)) {
            this.remove(item)
        } else {
            false
        }

fun List<*>.deepEquals(other : List<*>) =
        this.size == other.size && this.mapIndexed { index, element -> element == other[index] }.all { it }

