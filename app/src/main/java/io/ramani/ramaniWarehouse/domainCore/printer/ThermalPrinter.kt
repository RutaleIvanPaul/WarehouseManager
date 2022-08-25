package io.ramani.ramaniWarehouse.domainCore.printer

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.nio.ByteBuffer
import kotlin.experimental.or

class ThermalPrinter(os: OutputStream) {
    private val TAG = "ThermalPrinter"

    // Pulled from calling PeripheralManager.getUartDeviceList().
    private val UART_DEVICE_NAME = "USB1-1.2"
    private var os: OutputStream?
    private val PRINTER_INITIALIZE = byteArrayOf(0x1B, 0x40)
    private val ESC_CHAR: Byte = 0x1B
    private val PRINTER_SET_LINE_SPACE_24 = byteArrayOf(ESC_CHAR, 0x33, 24)

    // Slowing down the printer a little and increasing dot density, in order to make the QR
    // codes darker (they're a little faded at default settings).
    // Bytes represent the following: (first two): Print settings.
    // Max heating dots: Units of 8 dots.  11 means 88 dots.
    // Heating time: Units of 10 uS.  120 means 1.2 milliseconds.
    // Heating interval: Units of 10 uS. 50 means 0.5 milliseconds.
    private val PRINTER_DARKER_PRINTING = byteArrayOf(0x1B, 0x37, 11, 0x7F, 50)
    private val PRINTER_PRINT_AND_FEED = byteArrayOf(0x1B, 0x64)
    private val BYTE_LF: Byte = 0xA
    private val lock = Object()

    companion object {
        private val PRINTER_SELECT_BIT_IMAGE_MODE = byteArrayOf(0x1B, 0x2A, 33)
    }
    // Config settings for Ada 597 thermal printer.
    init {
        configurePrinter(os)
        this.os = os
    }

    private fun configurePrinter(os: OutputStream) {
        val config = outputStream
        writeToPrinterBuffer(config, PRINTER_INITIALIZE)
        writeToPrinterBuffer(config, PRINTER_DARKER_PRINTING)
        print(config)
    }

    private fun writeToPrinterBuffer(printerBuffer: ByteArrayOutputStream, command: ByteArray) {
        try {
            printerBuffer.write(command)
        } catch (e: IOException) {
            Log.d(TAG, "IO Exception while writing printer data to buffer.", e)
        }
    }

    private fun addLineFeed(printerBuffer: ByteArrayOutputStream, numLines: Int) {
        try {
            if (numLines <= 1) {
                printerBuffer.write(BYTE_LF.toInt())
            } else {
                printerBuffer.write(PRINTER_PRINT_AND_FEED)
                printerBuffer.write(numLines)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun print(output: ByteArrayOutputStream) {
        try {
            writeUartData(output.toByteArray())
        } catch (e: IOException) {
            Log.d(TAG, "IO Exception while printing.", e)
        }
    }

    fun printImage(bitmap: Bitmap) {
        if (os == null) {
            return
        }

        val width = bitmap.width
        val height = bitmap.height
        val controlByte = byteArrayOf((0x00ff and width).toByte(), (0xff00 and width shr 8).toByte())
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)

        val BAND_HEIGHT = 24

        // Bands of pixels are sent that are 8 pixels high.  Iterate through bitmap
        // 24 rows of pixels at a time, capturing bytes representing vertical slices 1 pixel wide.
        // Each bit indicates if the pixel at that position in the slice should be dark or not.
        var row = 0
        while (row < height) {
            try {
                val imageData = outputStream
                writeToPrinterBuffer(imageData, PRINTER_SET_LINE_SPACE_24)

                // Need to send these two sets of bytes at the beginning of each row.
                writeToPrinterBuffer(imageData, PRINTER_SELECT_BIT_IMAGE_MODE)
                writeToPrinterBuffer(imageData, controlByte)

                // Columns, unlike rows, are one at a time.
                for (col in 0 until width) {
                    val bandBytes = byteArrayOf(0x0, 0x0, 0x0)

                    // Ugh, the nesting of forloops.  For each starting row/col position, evaluate
                    // each pixel in a column, or "band", 24 pixels high.  Convert into 3 bytes.
                    for (rowOffset in 0..7) {

                        // Because the printer only maintains correct height/width ratio
                        // at the highest density, where it takes 24 bit-deep slices, process
                        // a 24-bit-deep slice as 3 bytes.
                        val pixelSlice = IntArray(3)
                        val pixel2Row = row + rowOffset + 8
                        val pixel3Row = row + rowOffset + 16

                        // If we go past the bottom of the image, just send white pixels so the printer
                        // doesn't do anything.  Everything still needs to be sent in sets of 3 rows.
                        Log.e("Thermal printer ***", "[row: ${row}, height:${height}]")

                        pixelSlice[0] = bitmap.getPixel(col, row + rowOffset)
                        pixelSlice[1] =
                            if (pixel2Row >= bitmap.height) Color.WHITE else bitmap.getPixel(
                                col,
                                pixel2Row
                            )
                        pixelSlice[2] =
                            if (pixel3Row >= bitmap.height) Color.WHITE else bitmap.getPixel(
                                col,
                                pixel3Row
                            )

                        val isDark = booleanArrayOf(
                            pixelSlice[0] == Color.BLACK,
                            pixelSlice[1] == Color.BLACK,
                            pixelSlice[2] == Color.BLACK
                        )

                        // Towing that fine line between "should I forloop or not".  This will only
                        // ever be 3 elements deep.
                        if (isDark[0]) bandBytes[0] = bandBytes[0] or (1 shl 7 - rowOffset).toByte()
                        if (isDark[1]) bandBytes[1] = bandBytes[1] or (1 shl 7 - rowOffset).toByte()
                        if (isDark[2]) bandBytes[2] = bandBytes[2] or (1 shl 7 - rowOffset).toByte()
                    }
                    writeToPrinterBuffer(imageData, bandBytes)
                }
                addLineFeed(imageData, 1)
                print(imageData)
                row += BAND_HEIGHT
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun printEmptyLines(lines: Int) {
        if (os == null) {
            return
        }
        val printerBuffer = outputStream
        addLineFeed(printerBuffer, lines)
        print(printerBuffer)
    }

    fun printLn(text: String) {
        if (os == null) {
            return
        }
        // The EscPosBuilder will take our formatted text and convert it to a byte array
        // understood as instructions and data by the printer.
        val printerBuffer = outputStream
        writeToPrinterBuffer(printerBuffer, text.toByteArray())
        addLineFeed(printerBuffer, 1)
        print(printerBuffer)
    }

    @Synchronized
    @Throws(IOException::class)
    private fun writeUartData(data: ByteArray) {
        if (os == null) {
            return
        }

        // If printer isn't initialized, abort.
        if (os == null) {
            return
        }

        // In the case of writing images, let's assume we shouldn't send more than 400 bytes
        // at a time to avoid buffer overrun - At which point the thermal printer tends to
        // either lock up or print garbage.
        val DEFAULT_CHUNK_SIZE = 400
        val chunk = ByteArray(DEFAULT_CHUNK_SIZE)
        val byteBuffer = ByteBuffer.wrap(data)

        while (byteBuffer.remaining() > DEFAULT_CHUNK_SIZE) {
            byteBuffer[chunk]
            os!!.write(chunk, 0, chunk.size)
            try {
                lock.wait(10)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        if (byteBuffer.hasRemaining()) {
            val lastChunk = ByteArray(byteBuffer.remaining())
            byteBuffer[lastChunk]
            os!!.write(lastChunk, 0, lastChunk.size)
        }
    }

    fun close() {
        if (os != null) {
            try {
                os!!.close()
                os = null
            } catch (e: IOException) {
                Log.w(TAG, "Unable to close UART device", e)
            }
        }
    }

    private val outputStream: ByteArrayOutputStream
        private get() = ByteArrayOutputStream()

    fun printQrCode(qrBitmap: Bitmap, size: Int, label: String?) {
        if (os == null) {
            return
        }
        try {
            Log.d(TAG, "Width: " + qrBitmap.width + ", Height: " + qrBitmap.height)
            if (label != null && !label.isEmpty()) {
                printLn(label)
                printEmptyLines(1)
            }
            printImage(qrBitmap)
            printEmptyLines(1)
        } catch (e: Exception) {
            Log.d(TAG, "Exception: ", e)
        }
    }
}
