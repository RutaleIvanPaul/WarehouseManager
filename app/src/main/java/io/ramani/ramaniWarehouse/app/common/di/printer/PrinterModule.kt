package io.ramani.ramaniWarehouse.app.common.di.printer

import io.ramani.ramaniWarehouse.domainCore.printer.PX400Printer
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

val printerModule = Kodein.Module("printerModule"){
//    bind<PX400Printer>() with singleton {
//        PX400Printer()
//    }
}