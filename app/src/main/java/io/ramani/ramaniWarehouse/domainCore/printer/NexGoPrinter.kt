package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.cloudpos.DeviceException
import com.cloudpos.POSTerminal
import com.cloudpos.printer.Format
import com.cloudpos.printer.PrinterDevice
import com.nexgo.oaf.apiv3.APIProxy
import com.nexgo.oaf.apiv3.device.printer.AlignEnum
import com.nexgo.oaf.apiv3.device.printer.DotMatrixFontEnum
import com.nexgo.oaf.apiv3.device.printer.FontEntity
import com.nexgo.oaf.apiv3.device.printer.Printer


class NexGoPrinter(var context: Context) : PrinterInterface {
    private var nexGoPrinter: Printer? = null
    private val TAG = "NexGo Printer"


    init{
        val engine = APIProxy.getDeviceEngine(context)
        nexGoPrinter = engine.printer

        nexGoPrinter?.appendPrnStr("Sample Printing",1, AlignEnum.CENTER, true)

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

    override fun printText(msg: String?) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_FONT_SIZE, Format.FORMAT_FONT_SIZE_MEDIUM)
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            nexGoPrinter?.appendPrnStr(msg, FontEntity(DotMatrixFontEnum.ASC_MYuen_16X48), AlignEnum.CENTER)
        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    override fun printBitmap(bitmap: Bitmap){
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            format.setParameter(Format.FORMAT_FONT_SIZE_EXTRASMALL, Format.FORMAT_FONT_SIZE_EXTRASMALL)
            nexGoPrinter?.appendImage(bitmap,AlignEnum.CENTER )
        } catch (ex: DeviceException) {
            ex.printStackTrace()
        }
    }

}