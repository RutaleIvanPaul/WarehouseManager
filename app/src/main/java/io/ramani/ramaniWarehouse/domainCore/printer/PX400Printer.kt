package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice


class PX400Printer(var context: Context) {
    private var device: POSDevice? = null
    private val TAG = "Printer Work"

    fun open() {
        try {
            device?.open()
            Log.d(TAG,"Open Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    fun close() {
        try {
            device?.close()
            Log.d(TAG,"Close Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Close Printer Failed!")
            ex.printStackTrace()
        }
    }

    fun printText(msg: String?) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_FONT_SIZE, Format.FORMAT_FONT_SIZE_MEDIUM)
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            device?.printText(format, msg)
            Log.d(TAG,"Print Text  succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    fun printBitmap(bitmap: Bitmap){
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            format.setParameter(Format.FORMAT_FONT_SIZE_EXTRASMALL, Format.FORMAT_FONT_SIZE_EXTRASMALL)
            device?.printBitmap(format,bitmap)
            Log.d(TAG,"Print Bitmap  succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Bitmap Failed!")
            ex.printStackTrace()
        }
    }

    fun getDevice(device: String): POSDevice? {
        when (device) {
            Manufacturer.wizarPOS.toString() -> return FamocoDevice(context)
            Manufacturer.NexGo.toString() -> return NexGoDevice(context)
            Manufacturer.MobiIot.toString() -> return MobiIoTDevice(context)
            Manufacturer.MobiWire.toString() -> return MobiIoTDevice(context)
            else -> return null
        }
    }

    init {
        val name = Build.MANUFACTURER
        if (device == null) {
            device = getDevice(name)
        }
    }
}