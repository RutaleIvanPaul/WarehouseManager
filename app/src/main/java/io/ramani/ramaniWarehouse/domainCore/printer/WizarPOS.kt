package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice


class WizarPOS(var context: Context) : POSDevice{
    private var device: PrinterDevice? = null
    private val TAG = "Printer Work"

    override fun device(): PrinterDevice {
        val printerDevice = POSTerminal.getInstance(context)
            .getDevice("cloudpos.device.printer")
        if(printerDevice != null) {
            device = printerDevice as PrinterDevice
        }
        return device !!
    }

    override fun open() {
        try {
            if(device is PrinterDevice) device?.open()
            else {
                val printerDevice = POSTerminal.getInstance(context)
                    .getDevice("cloudpos.device.printer")
                if(printerDevice != null) {
                    device = printerDevice as PrinterDevice
                    device?.open()
                }
            }

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

    override fun printBitmap(bitmap: Bitmap){
        try {
            val newFormat = Format()
            newFormat.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            newFormat.setParameter(Format.FORMAT_FONT_SIZE_EXTRASMALL, Format.FORMAT_FONT_SIZE_EXTRASMALL)
            device?.printBitmap(newFormat,bitmap)
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