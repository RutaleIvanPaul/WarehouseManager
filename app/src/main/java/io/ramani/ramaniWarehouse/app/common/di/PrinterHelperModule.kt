package io.ramani.ramaniWarehouse.app.common.di

import io.ramani.ramaniWarehouse.domainCore.printer.BluetoothConnection
import io.ramani.ramaniWarehouse.domainCore.printer.PrinterHelper
import io.ramani.ramaniWarehouse.domainCore.printer.ThermalPrinter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.OutputStream

val printerHelperModule = Kodein.Module("printerHelperModule"){
    bind<PrinterHelper>() with singleton {
        PrinterHelper(instance())
    }

//    bind<ThermalPrinter>() with singleton {
//        ThermalPrinter(instance("bluetoothOutputStream"))
//    }
//
//    bind<OutputStream>("bluetoothOutputStream") with singleton {
//        BluetoothConnection.instanceBluetoothSocket!!.outputStream
//    }
}