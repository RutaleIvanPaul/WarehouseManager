package io.ramani.ramaniWarehouse.domainCore.printer

import android.graphics.Bitmap
import android.graphics.Color

fun Bitmap.processForPrinting(): Bitmap {
    return Bitmap.createScaledBitmap(this,300,300, false).trimBorders(Color.WHITE)
}

fun Bitmap.processForPrintingOnMobiWireDevice(): Bitmap {
    return Bitmap.createScaledBitmap(this,384,this.height, false).trimBorders(Color.WHITE)
}

fun Bitmap.trimBorders(color: Int): Bitmap {
    var startX = 0
    loop@ for (x in 0 until width) {
        for (y in 0 until height) {
            if (getPixel(x, y) != color) {
                startX = x
                break@loop
            }
        }
    }
    var startY = 0
    loop@ for (y in 0 until height) {
        for (x in 0 until width) {
            if (getPixel(x, y) != color) {
                startY = y
                break@loop
            }
        }
    }
    var endX = width - 1
    loop@ for (x in endX downTo 0) {
        for (y in 0 until height) {
            if (getPixel(x, y) != color) {
                endX = x
                break@loop
            }
        }
    }
    var endY = height - 1
    loop@ for (y in endY downTo 0) {
        for (x in 0 until width) {
            if (getPixel(x, y) != color) {
                endY = y
                break@loop
            }
        }
    }

    val newWidth = endX - startX + 1
    val newHeight = endY - startY + 1

    return Bitmap.createBitmap(this, startX, startY, newWidth, newHeight)
}