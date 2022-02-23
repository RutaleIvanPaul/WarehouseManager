package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.device.printer.AlignEnum
import com.nexgo.oaf.apiv3.device.printer.DotMatrixFontEnum
import com.nexgo.oaf.apiv3.device.printer.FontEntity

class NexGoDevice(val context: Context) : POSDevice {
    val engine = APIProxy.getDeviceEngine(context)
    private var nexGoPrinter  = engine.printer
    private val TAG = "NexGo Printer Work"


    override fun device(): Any {
        val engine = APIProxy.getDeviceEngine(context)
        val device = engine.printer
        device.initPrinter()
        return device
    }


    override fun open() {
        try {
            nexGoPrinter?.initPrinter()
        } catch (ex: DeviceException) {
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
            nexGoPrinter?.appendPrnStr(msg, FontEntity(DotMatrixFontEnum.ASC_MYuen_16X48), AlignEnum.CENTER)
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    override fun printBitmap(format: Format?, bitmap: Bitmap){
        try {
            nexGoPrinter?.appendImage(bitmap, AlignEnum.CENTER )
        } catch (ex: DeviceException) {
            ex.printStackTrace()
        }
    }

}