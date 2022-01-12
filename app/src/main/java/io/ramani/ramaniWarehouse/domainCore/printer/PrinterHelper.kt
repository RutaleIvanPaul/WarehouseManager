package io.ramani.ramaniWarehouse.domainCore.printer

import android.graphics.Bitmap
import android.os.Build

class PrinterHelper(
    private val pX400Printer: PX400Printer
//    private val thermalPrinter: ThermalPrinter
) {
    fun isFamocoDevice(): Boolean {
        return Build.MANUFACTURER == "wizarPOS"
    }

    fun open(){
        if(isFamocoDevice()){
            pX400Printer.open()
        }
    }

    fun printBitmap(bitmap: Bitmap) {
        if(isFamocoDevice()){
            pX400Printer.printBitmap(bitmap)
        }
    }


}