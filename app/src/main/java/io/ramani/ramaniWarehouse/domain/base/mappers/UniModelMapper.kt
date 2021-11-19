package io.ramani.ramaniWarehouse.domain.base.mappers

/**
 * Created by Ahmed Mahmoud on 17/12/18.
 */
interface UniModelMapper<in From, out To> {
    fun mapFrom(from: From): To
}