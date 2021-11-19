package io.ramani.ramaniWarehouse.domain.base.mappers

/**
 * Created by Amr on 11/8/17.
 */
class DefaultValueModelMapper<From, To>(private val defaultFrom: From,
                                        private val defaultTo: To) : ModelMapper<From, To> {
    override fun mapFrom(from: From): To = defaultTo

    override fun mapTo(to: To): From = defaultFrom
}