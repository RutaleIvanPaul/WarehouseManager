package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice


interface PrinterInterface {
    fun open()

    fun close()

    fun printText(msg: String?)

    fun printBitmap(bitmap: Bitmap)

}