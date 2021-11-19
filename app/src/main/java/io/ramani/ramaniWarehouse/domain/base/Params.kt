package io.ramani.ramaniWarehouse.domain.base

/**
 * Created by Amr on 6/1/17.
 */

@Deprecated("This Interface should not be used anymore check io.ramani.ramaniWarehouse.domain.base.v2.Params")
open class Params(val map: Map<String, *>) {
    constructor(vararg pairs: Pair<String, *>) : this(pairs.toMap())

    inline fun <reified T> get(key: String, defaultValue: T? = null): T {
        return if (map.containsKey(key)) {
            map[key].takeIf { it is T } as T
        } else {
            defaultValue!!
        }
    }

    fun contains(key: String) =
            map[key] != null

    override fun toString(): String = map.toString()

    override fun equals(other: Any?): Boolean = when (other) {
        null -> false
        is Params -> map == other.map
        else -> false
    }

    override fun hashCode(): Int = map.hashCode()
}

fun emptyParams(): Params = Params(mapOf<String, Any>())