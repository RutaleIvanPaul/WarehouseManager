package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice


class PX400Printer(var context: Context) {
    private var device: PrinterDevice? = null
    fun open() {
        try {
            device?.open()
            Log.d("Printer Work","Open Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d("Printer Work","Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    fun close() {
        try {
            device?.close()
            Log.d("Printer Work","Close Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d("Printer Work","Close Printer Failed!")
            ex.printStackTrace()
        }
    }

    fun printText(msg: String?) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_FONT_SIZE, Format.FORMAT_FONT_SIZE_MEDIUM)
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            device?.printText(format, msg)
            Log.d("Printer Work","Print Text  succeed!")
        } catch (ex: DeviceException) {
            Log.d("Printer Work","Print Text Failed!")
            ex.printStackTrace()
        }
    }

    fun printBitmap(bitmap: Bitmap){
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            format.setParameter(Format.FORMAT_FONT_SIZE_EXTRASMALL, Format.FORMAT_FONT_SIZE_EXTRASMALL)
            device?.printBitmap(format,bitmap)
            Log.d("Printer Work","Print Bitmap  succeed!")
        } catch (ex: DeviceException) {
            Log.d("Printer Work","Print Bitmap Failed!")
            ex.printStackTrace()
        }
    }

    init {
        if (device == null) {
            device = POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.printer") as PrinterDevice
        }
    }
}