package io.ramani.ramaniWarehouse.domainCore.printer

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import java.lang.Exception
import com.cloudpos.printer.PrinterDevice


class PrinterAction {
    private var device: PrinterDevice? = null
    fun open(param: Map<String?, Any?>?, mContext: Context?, callback: ActionCallback) {
        try {
            if (device == null) {
                device = POSTerminal.getInstance(mContext)
                    .getDevice("cloudpos.device.printer") as PrinterDevice
                device.open()
            } else {
                device.open()
            }
            callback.sendResponse(200)
        } catch (e: DeviceException) {
            e.printStackTrace()
            callback.sendResponse(500)
        }
    }

    fun printText(message: String?, callback: ActionCallback) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_FONT_SIZE, Format.FORMAT_FONT_SIZE_MEDIUM)
            format.setParameter(Format.FORMAT_ALIGN_LEFT, Format.FORMAT_ALIGN_LEFT)
            device.printText(format, message)
            callback.sendResponse(200)
        } catch (e: DeviceException) {
            e.printStackTrace()
            callback.sendResponse(500)
        }
    }

    fun sendESCCommand(param: Map<String?, Any?>?, callback: ActionCallback?) {
        val command = byteArrayOf(
            0x12.toByte(), 0x54.toByte()
        )
        try {
            device.sendESCCommand(command)
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
    }

    fun queryStatus(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val status: Int = device.queryStatus()
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
    }

    fun cutPaper(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device.cutPaper()
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
    }

    fun printBitmap(bitmap: Bitmap?, callback: ActionCallback) {
        try {
            val format = Format()
            format.setParameter(Format.FORMAT_ALIGN, Format.FORMAT_ALIGN_CENTER)
            format.setParameter(Format.FORMAT_FONT_SIZE_SMALL, Format.FORMAT_FONT_SIZE_SMALL)
            device.printBitmap(format, bitmap)
            callback.sendResponse(200)
        } catch (e: DeviceException) {
            e.printStackTrace()
            callback.sendResponse(500)
        }
    }

    fun printHtml(param: Map<String?, Any?>?, mContext: Context?, callback: ActionCallback?) {
        try {
            val htmlContent =
                """
                The 1st mobile payment solution based on WAP in the world in 2001.
                
                <h1>The 1st PCI certified handheld EFT POS in the world in 2005.</h1>
                
                The 1st secure embedded Java platform accredited by the payment industry.
                
                <h2>The 1st GlobalPlatform & STIP compliant POS product in mass production in 2006.</h2>
                
                The 1st cloud based and GlobalPlatform compliant device management and multi-application provisioning system app store for POS terminal.
                
                The 1st generation e-commerce, payment gateway and e-wallet products based on Secure Electronic Transaction (SET) protocol in 1997.
                
                The 1st generation key management system for EMV chip card in China in 1998.
                
                <h5>The 1st digital certificate system CA in China.<h5>
                
                The 1st online merchant in China.
                
                The 1st internet banking and payment system in China
                
                
                
                """.trimIndent()
            Handler(Looper.getMainLooper()).post {
                try {
                    device.printHTML(mContext, htmlContent, null)
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun printBarcode(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            val format = Format()
            format.setParameter("HRI-location", "up")
            //0, 01234567896
            //1, "04310000526"
            //
            device.printBarcode(format, PrinterDevice.BARCODE_CODE128, "000023")
            device.printText("\n\n\n")
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
    }

    fun cancelRequest(param: Map<String?, Any?>?, callback: ActionCallback?) {
        try {
            device.cancelRequest()
        } catch (e: DeviceException) {
            e.printStackTrace()
        }
    }

    fun close(param: Map<String?, Any?>?, callback: ActionCallback) {
        try {
            device.close()
            callback.sendResponse(200)
        } catch (e: DeviceException) {
            e.printStackTrace()
            callback.sendResponse(500)
        }
    }
}

