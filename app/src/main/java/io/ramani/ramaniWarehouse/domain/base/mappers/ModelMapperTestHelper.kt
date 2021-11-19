package io.ramani.ramaniWarehouse.domain.base.mappers

/**
 * Created by Amr on 10/10/17.
 */
open class ModelMapperTestHelper<From, To>(private val modelMapper: ModelMapper<From, To>) : ModelMapper<From, To> {
    override fun mapFrom(from: From): To = modelMapper.mapFrom(from)

    override fun mapTo(to: To): From = modelMapper.mapTo(to)
}