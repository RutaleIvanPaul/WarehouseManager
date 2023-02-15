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

    fun open(): Boolean {
        return pX400Printer.open()
    }

    fun close(): String {
        pX400Printer.close()
        return getString(R.string.no_printer)
    }

    fun printBitmap(bitmap: Bitmap): PrintStatus {
        if (open()) {
            pX400Printer.printBitmap(bitmap)
            close()
            return PrintStatus(true)
        }else{
            return PrintStatus(false, getString(R.string.no_printer))
        }

    }

    fun printText(string: String): PrintStatus {
        if (open()) {
            pX400Printer.printText(string)
            close()
            return PrintStatus(true)
        }else{
            return PrintStatus(false,getString(R.string.no_printer))
        }
    }


}