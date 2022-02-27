package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice
import com.mobiiot.androidqapi.api.CsDevice
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil
import com.nexgo.oaf.apiv3.APIProxy
import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider
import com.mobiiot.androidqapi.api.CsPrinter
import com.sagereal.printer.PrinterInterface

class MobiIoTDevice(val context: Context) : POSDevice {
    private var device: CsPrinter? = null
    private val TAG = "MobiIoT"
    var inter: PrinterInterface? = null

    // var device = PrinterServiceUtil.getPrinterService()


    override fun device(): Any {
        PrinterServiceUtil.bindService(context)
        inter = PrinterServiceUtil.getPrinterService()
         //PrinterInterface()
        device = CsPrinter()

        return device as CsPrinter
    }


    override fun open() {
        try {
            PrinterServiceUtil.bindService(context)
            PrinterServiceUtil.getPrinterService()
            PrinterServiceUtil.getPrintIntent()
           // CsDevice.getDeviceInformation()

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
            PrinterServiceUtil.bindService(context)
            PrinterServiceUtil.getPrinterService()
            PrinterServiceUtil.getPrintIntent()
            CsPrinter.printText(msg)
            CsPrinter.printEndLine()
            val errorMessage = CsPrinter.getLastError()


            Log.d("$TAG text error",errorMessage.toString())
            Log.d("$TAG printText","Print Text  succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    override fun printBitmap(format: Format?, bitmap: Bitmap){
        try {
//            val format = Format()
//            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
//            format.setParameter(Format.FORMAT_FONT_SIZE_EXTRASMALL, Format.FORMAT_FONT_SIZE_EXTRASMALL)
//            val printer = CsPrinter()
//            PrinterServiceUtil.bindService(context)
//            PrinterServiceUtil.getPrinterService()
//            PrinterServiceUtil.getPrintIntent()
//            CsPrinter.printBitmap(bitmap)
//            CsPrinter.printEndLine()
//            val errorMessage = CsPrinter.getLastError()
//
//
//            Log.d("$TAG bitmap error",errorMessage.toString())
//            Log.d("$TAG printText","Print Text  succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Bitmap Failed!")
            ex.printStackTrace()
        }
    }

    init {
        Log.d(TAG +"init","init")
            PrinterServiceUtil.bindService(context)
            PrinterServiceUtil.getPrinterService()
            device = CsPrinter()
        val printerStatus = CsPrinter.getPrinterStatus()

        Log.d(TAG +"init device",device.toString())
            Log.d(TAG +"init device info",CsDevice.getDeviceInformation().toString())
            Log.d(TAG +"init service", PrinterServiceUtil.getPrinterService().toString())
            Log.d(TAG +"init Printer",printerStatus.toString())





    }



}