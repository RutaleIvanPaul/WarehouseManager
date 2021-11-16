package io.ramani.ramaniWarehouse.app.common.di.datetime


import io.ramani.ramaniWarehouse.domain.datetime.DateTimeManager
import io.ramani.ramaniWarehouse.domain.datetime.DefaultDateTimeParser
import io.ramani.ramaniWarehouse.domain.datetime.DefaultFromServerDateTimeParser
import io.ramani.ramaniWarehouse.core.domain.datetime.*
import io.ramani.ramaniWarehouse.domain.datetime.DateFormatter
import io.ramani.ramaniWarehouse.domainCore.datetime.FromServerDateTimeParser
import io.ramani.ramaniWarehouse.domainCore.datetime.IDateTimeManager
import org.kodein.di.Kodein
import org.kodein.di.generic.*

/**
 * Created by Raed on 9/22/17.
 */
val dateTimeModule = Kodein.Module("dateTimeModule") {
    bind<DateTimeParser>() with provider {
        DefaultDateTimeParser(instance())
    }

    bind<FromServerDateTimeParser>() with provider {
        DefaultFromServerDateTimeParser()
    }

    bind<DateFormatter>() with provider {
        DateFormatter(instance())
    }

    bind<IDateTimeManager>() with singleton {
        DateTimeManager()
    }
}