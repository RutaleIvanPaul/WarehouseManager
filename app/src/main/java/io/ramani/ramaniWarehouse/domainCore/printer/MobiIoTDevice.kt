package io.ramani.ramaniWarehouse.domainCore.printer

import android.R
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
import android.R.attr.bitmap
import java.io.ByteArrayInputStream


class MobiIoTDevice(val context: Context) : POSDevice {
    private val TAG = "MobiIoT"

     var device = CsPrinter()


    override fun device(): Any {
        return CsPrinter()
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

        } catch (ex: DeviceException) {
            Log.d(TAG,"Close Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun printText(format: Format?, msg: String?) {
        try {
            CsPrinter.printText(msg)
            val errorMessage = CsPrinter.getLastError()
            CsPrinter.printEndLine()
            Log.d("$TAG text error",errorMessage.toString())

        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    override fun printBitmap(bitmap: Bitmap){
        try {

              val newBitmap =  bitmap.processForPrintingOnMobiWireDevice(bitmap.height)

            if (!Build.MODEL.contains("MPE")) {

                CsPrinter.printSetDarkness(2)

                CsPrinter.printBitmap(newBitmap, 0)
                CsPrinter.printEndLine()
                CsPrinter.printEndLine()

            } else {
                Toast.makeText(context, "2", Toast.LENGTH_LONG).show()
            }

        } catch (ex: DeviceException) {
            ex.printStackTrace()
        }
    }

    fun Bitmap.toByteArray():ByteArray{
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG,100,this)
            return toByteArray()
        }
    }
    init {
        //device()
        Log.e("Build", Build.MODEL)

    }


}