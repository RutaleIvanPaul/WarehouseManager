package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.printer.Format
import com.mobiiot.androidqapi.api.CsPrinter
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil

import io.ramani.ramaniWarehouse.R
import io.ramani.ramaniWarehouse.app.common.presentation.language.StringProvider

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class MobiIoTDevice(val context: Context) : POSDevice {
    private val TAG = "MobiIoT"

     var device = CsPrinter()


    override fun device(): Any {
       // PrinterServiceUtil.bindService(context)
       // inter = PrinterServiceUtil.getPrinterService()
         //PrinterInterface()


        return CsPrinter()
    }


    override fun open() {
        try {
            PrinterServiceUtil.bindService(context)
//            PrinterServiceUtil.getPrinterService()
//            PrinterServiceUtil.getPrintIntent()
           // CsDevice.getDeviceInformation()

            Log.d(TAG,"Open Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun close() {
        try {
            CsPrinter.printEndLine()
        } catch (ex: DeviceException) {
            Log.d(TAG,"Close Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun printText(format: Format?, msg: String?) {
        try {
            CsPrinter.printText(msg)
            val errorMessage = CsPrinter.getLastError()


            Log.d("$TAG text error",errorMessage.toString())

        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    override fun printBitmap(format: Format?, bitmap: Bitmap){
        try {
            //CsPrinter.printBitmap(bitmap,0)
            val errorMessage = CsPrinter.getLastError()

            Log.d("$TAG text error",errorMessage.toString())

            var inputStreamToByte: ByteArray? = null
            try {
               // inputStreamToByte = InputStreamToByte(bitmap)
                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
                inputStreamToByte = bos.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (!Build.MODEL.contains("MPE")) {

                val result = CsPrinter.printBitmap(inputStreamToByte, 0)
                CsPrinter.printBitmap(inputStreamToByte, 0)
                CsPrinter.printBitmap(inputStreamToByte, 0)
                Log.e("print result bitmap", result.toString() + "")
            } else {
                CsPrinter.printBitmapMPE(inputStreamToByte, 0)
            }

        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Bitmap Failed!")
            ex.printStackTrace()
        }
    }

    fun inputStreamToByte(`is`: InputStream): ByteArray? {
        val bytestream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var ch: Int
        while (`is`.read(buffer).also { ch = it } != -1) {
            bytestream.write(buffer, 0, ch)
        }
        val data = bytestream.toByteArray()
        bytestream.close()
        return data
    }

    init {
        //device()
        Log.e("Build", Build.MODEL)

    }



}