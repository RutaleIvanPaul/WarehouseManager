package io.ramani.ramaniWarehouse.app.common.di.pagination

import io.ramani.ramaniWarehouse.data.common.source.remote.mappers.PaginationMetaRemoteModelMapper
import io.ramani.ramaniWarehouse.data.entities.PaginationMetaRemote
import io.ramani.ramaniWarehouse.domain.base.mappers.ModelMapper
import io.ramani.ramaniWarehouse.domain.entities.PaginationMeta
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

val paginationModule = Kodein.Module("paginationModule") {
    bind<ModelMapper<PaginationMetaRemote, PaginationMeta>>() with provider {
        PaginationMetaRemoteModelMapper()
    }
}