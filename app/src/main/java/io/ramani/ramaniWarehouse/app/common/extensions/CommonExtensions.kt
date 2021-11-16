package io.ramani.ramaniWarehouse.app.common.extensions

inline fun <reified T> Any.takeIfType(action: (T) -> Unit) {
    takeIf { it is T }?.let { action(it as T) }
}

inline fun <reified T, R> Any.letIfType(action: (T) -> R): R? {
    return takeIf { it is T }?.let { action(it as T) }
}