package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.cloudpos.DeviceException
import com.cloudpos.printer.Format
import com.mobiiot.androidqapi.api.CsPrinter
import com.mobiiot.androidqapi.api.MobiiotAPI


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
           // PrinterServiceUtil.bindService(context)
//            PrinterServiceUtil.getPrinterService()
//            PrinterServiceUtil.getPrintIntent()
           // CsDevice.getDeviceInformation()
            MobiiotAPI(context)

            Log.d(TAG,"Open Printer succeed!")
        } catch (ex: DeviceException) {
            Log.d(TAG,"Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun close() {
        try {
            //CsPrinter.printEndLine()
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
           // val is_ticket = context.resources.openRawResource(bitmap)
              val newBitmap =  CsPrinter.getResizedBitmap(bitmap, bitmap.width, bitmap.height)
            CsPrinter.printSetDarkness(1)

            CsPrinter.printBitmap(newBitmap, 0)

//            val errorMessage = CsPrinter.getLastError()
//
//            Log.d("$TAG text error",errorMessage.toString())
//
//            var inputStreamToByte: ByteArray? = null
//            try {
//               // inputStreamToByte = InputStreamToByte(bitmap)
//                //Convert bitmap to byte array
//                inputStreamToByte = bitmap.toByteArray()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                CsPrinter.printBitmap(bitmap)
//            }
            if (!Build.MODEL.contains("MPE")) {

               // val result = CsPrinter.printBitmap(inputStreamToByte, 0)
                CsPrinter.printBitmap(newBitmap, 0)
                //CsPrinter.printBitmap(inputStreamToByte, 0)
               // Log.e("print result bitmap", result.toString() + "")
                Toast.makeText(context, "1", Toast.LENGTH_LONG).show()
            } else {
                CsPrinter.printBitmapMPE(newBitmap.toByteArray(), 0)
                Toast.makeText(context, "2", Toast.LENGTH_LONG).show()

            }

        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Bitmap Failed!")
            ex.printStackTrace()
        }
    }

    fun Bitmap.toByteArray():ByteArray{
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG,10,this)
            return toByteArray()
        }
    }
    init {
        //device()
        Log.e("Build", Build.MODEL)

    }



}