package io.ramani.ramaniWarehouse.domain.base

/**
 * Created by Amr on 11/28/17.
 */
abstract class BaseRepository {
    protected open var cacheInvalid: Boolean = true

    open fun setCacheValid(valid: Boolean) {
        cacheInvalid = !valid
    }

    open fun isCacheInvalid() = cacheInvalid
}