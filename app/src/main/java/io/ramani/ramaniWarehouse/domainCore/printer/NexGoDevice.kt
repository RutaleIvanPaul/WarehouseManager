package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.nexgo.oaf.apiv3.APIProxy

import com.nexgo.oaf.apiv3.DeviceEngine

import com.nexgo.oaf.apiv3.SdkResult

import com.nexgo.oaf.apiv3.device.printer.AlignEnum

import com.nexgo.oaf.apiv3.device.printer.Printer


import java.io.ByteArrayOutputStream
import com.cloudpos.printer.Format


class NexGoDevice(val context: Context) : POSDevice {
    private val TAG = "NexGo"
    var deviceEngine: DeviceEngine? = null
    var device: Printer? = null


    override fun device(): Any {
        deviceEngine = APIProxy.getDeviceEngine(context)
        device = deviceEngine?.getPrinter()
        device?.initPrinter()
        device?.setTypeface(Typeface.SERIF)
        device?.setLetterSpacing(4)
        return device as Printer
    }


    override fun open() {
        try {

            Log.d(TAG,"Open Printer succeed!")
        } catch (ex: Exception) {
            Log.d(TAG,"Open Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun close() {
        try {
            device!!.startPrint(false) { i: Int -> device!!.cutPaper() }

        } catch (ex: Exception) {
            Log.d(TAG,"Close Printer Failed!")
            ex.printStackTrace()
        }
    }

    override fun printText(format: Format?, msg: String?) {
        val stati = device!!.status
        when (stati) {
            SdkResult.Success -> device!!.appendPrnStr(msg, 19, AlignEnum.LEFT, false)
            SdkResult.Printer_PaperLack -> {
                Toast.makeText(context, "No paper", Toast.LENGTH_LONG).show()
                println("no paper")
            }
            SdkResult.Printer_TooHot -> {
                Toast.makeText(context, "Printer is too hot", Toast.LENGTH_LONG).show()
                println("Printer is too hot")
            }
            SdkResult.Fail -> {
                Toast.makeText(context, "Print failed", Toast.LENGTH_LONG).show()
                println("print failed")
            }
            else -> {
                Toast.makeText(context, "See technicians", Toast.LENGTH_LONG).show()
                println("See technicians")
            }
        }

    }

    override fun printBitmap(bitmap: Bitmap){

        val stati = device!!.status
        when (stati) {
            SdkResult.Success -> device!!.appendImage(bitmap, AlignEnum.CENTER)
            SdkResult.Printer_PaperLack -> {
                Toast.makeText(context, "No paper", Toast.LENGTH_LONG).show()
                println("no paper")
            }
            SdkResult.Printer_TooHot -> {
                Toast.makeText(context, "Printer is too hot", Toast.LENGTH_LONG).show()
                println("Printer is too hot")
            }
            SdkResult.Fail -> {
                Toast.makeText(context, "Print failed", Toast.LENGTH_LONG).show()
                println("print failed")
            }
            else -> {
                Toast.makeText(context, "See technicians", Toast.LENGTH_LONG).show()
                println("See technicians")
            }
        }

    }

    fun Bitmap.toByteArray():ByteArray{
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG,100,this)
            return toByteArray()
        }
    }
    init {
        Log.e("Build", Build.MODEL)
        if(device == null){
            deviceEngine = APIProxy.getDeviceEngine(context)
            device = deviceEngine?.getPrinter()
            device?.initPrinter()
            device?.setTypeface(Typeface.SERIF)
            device?.setLetterSpacing(4)
        }

    }


}