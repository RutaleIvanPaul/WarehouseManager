//package io.ramani.ramaniWarehouse.domainCore.printer
//
//import android.content.Context
//import android.graphics.fonts.Font
//import android.graphics.fonts.FontFamily
//import android.os.Build
//import androidx.annotation.RequiresApi
//import com.nexgo.oaf.apiv3.APIProxy
//import com.nexgo.oaf.apiv3.device.printer.AlignEnum
//import com.nexgo.oaf.apiv3.device.printer.OnPrintListener
//import io.ramani.ramaniWarehouse.R
//
//class NexGoPrinterUtil(context: Context) : OnPrintListener {
//   // private lateinit var printer
//    val string: String? = null
//
//
//    init{
//        val engine = APIProxy.getDeviceEngine(context)
//        val printer = engine.printer
//        printer.initPrinter()
//        printer.appendPrnStr("Sample Printing",1, AlignEnum.CENTER, true)
//
//    }
//
//    fun printText(){
//        //printer.
//    }
//
//
//
//    override fun onPrintResult(p0: Int) {
//        TODO("Not yet implemented")
//    }
//
//
//}