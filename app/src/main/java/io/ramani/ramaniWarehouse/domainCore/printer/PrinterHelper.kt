package io.ramani.ramaniWarehouse.domainCore.printer

import android.graphics.Bitmap
import android.os.Build
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider.getString

class PrinterHelper(
    private val pX400Printer: PX400Printer
//    private val thermalPrinter: ThermalPrinter
) {
    fun isFamocoDevice(): Boolean {
        return Build.MANUFACTURER == "wizarPOS"
    }

    fun open(): String {
        pX400Printer.open()
        return getString(R.string.printer_opened)
    }

    fun close(): String {
        pX400Printer.close()
        return getString(R.string.no_printer)
    }

    fun printBitmap(bitmap: Bitmap): PrintStatus {
        open()
        pX400Printer.printBitmap(bitmap)
        close()
        return PrintStatus(true)

    }

    fun printText(string: String): PrintStatus {
        open()
        pX400Printer.printText(string)
        close()
        return PrintStatus(true)
    }


}