package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil
import com.nexgo.oaf.apiv3.APIProxy
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider
import com.mobiiot.androidqapi.api.CsPrinter

class MobiIoTDevice(val context: Context) : POSDevice {
    private var device = CsPrinter()
    private val TAG = "MobiIoT Printer Work"


    override fun device(): Any {
        PrinterServiceUtil.bindService(context)
        return device
    }

    override fun open() {
        try {

            Log.d(TAG,"Open Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun close() {
        try {
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
            CsPrinter.printText(msg)
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
            CsPrinter.printBitmap(bitmap)
            Log.d(TAG,"Print Bitmap  succeed!")
            Log.d(TAG ,CsPrinter.getPrinterStatus().toString())
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Bitmap Failed!")
            ex.printStackTrace()
        }
    }

    init {
        if (device == null) {
            PrinterServiceUtil.getPrinterService()
            device = CsPrinter()
            Log.d(TAG ,CsPrinter.getPrinterStatus().toString())

        }
    }



}