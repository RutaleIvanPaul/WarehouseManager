package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice
import com.nexgo.oaf.apiv3.APIProxy
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider

class FamocoDevice(val context: Context) : POSDevice {
    private var device: PrinterDevice? = null
    private val TAG = "Famoco Printer Work"


    override fun device(): Any {
        val printerDevice = POSTerminal.getInstance(context)
            .getDevice("cloudpos.device.printer")
        return printerDevice
    }

    override fun open() {
        try {
            device?.open()
            Log.d(TAG,"Open Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun close() {
        try {
            device?.close()
            Log.d(TAG,"Close Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Close Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun printText(format: Format?, msg: String?) {
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

    override fun printBitmap(format: Format?, bitmap: Bitmap){
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

    init {
        if (device == null) {
            val printerDevice = POSTerminal.getInstance(context)
                .getDevice("cloudpos.device.printer")
            if(printerDevice != null) {
                device = printerDevice as PrinterDevice
            }
        }
    }



}