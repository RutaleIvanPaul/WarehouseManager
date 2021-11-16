package io.ramani.ramaniWarehouse.domain.base.mappers

/**
 * Created by Amr on 11/8/17.
 */

infix fun <From : Any, To> From.mapFromWith(modelMapper: ModelMapper<From, To>) =
        modelMapper.mapFrom(this)

infix fun <From : Any, To> List<From>.mapFromWith(modelMapper: ModelMapper<From, To>) =
        map { it mapFromWith modelMapper }

infix fun <From : Any, To> From.mapFromWith(modelMapper: UniModelMapper<From, To>) =
        modelMapper.mapFrom(this)

infix fun <From : Any, To> List<From>.mapFromWith(modelMapper: UniModelMapper<From, To>) =
        map { it mapFromWith modelMapper }

infix fun <To : Any, From> To.mapToWith(modelMapper: ModelMapper<From, To>) =
        modelMapper.mapTo(this)

infix fun <To : Any, From> List<To>.mapToWith(modelMapper: ModelMapper<From, To>) =
        map { it mapToWith modelMapper }
