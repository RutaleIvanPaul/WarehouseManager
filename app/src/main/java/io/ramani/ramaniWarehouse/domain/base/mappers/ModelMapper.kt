package io.ramani.ramaniWarehouse.domain.base.mappers

/**
 * Created by Amr on 9/4/17.
 */
interface ModelMapper<From, To> {
    fun mapFrom(from: From): To

    fun mapTo(to: To): From
}