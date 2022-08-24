package io.ramani.ramaniWarehouse.domainCore.printer

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.cloudpos.DeviceException
import com.cloudpos.printer.Format
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class MobiIoTDevice(val context: Context) : POSDevice {
    private val TAG = "MobiIoT"

    private var mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mBluetoothSocket: BluetoothSocket
    private var mBluetooth = mBluetoothAdapter.getRemoteDevice("02:03:00:00:00:00")

    lateinit var thermalPrinter: ThermalPrinter

    init {
        device()
        Log.e("Build", Build.MODEL)

    }

    override fun device(): ThermalPrinter {
        //return CsPrinter()
        if (mBluetoothAdapter == null) {
            Toast.makeText(context, "No default adapter detected", Toast.LENGTH_SHORT).show()
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    context.startActivity(enableBtIntent)
                }
            } else {
                thermalPrinter = ThermalPrinter(getOs()!!)
            }
        }

        return thermalPrinter
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
            thermalPrinter.printLn(msg!!)
            Log.d(TAG,"Print Text Success!")

        } catch (ex: DeviceException) {
            Log.d(TAG,"Print Text Failed!")
            ex.printStackTrace()
        }
    }

    override fun printBitmap(bitmap: Bitmap){
        try {
            thermalPrinter.printImage(bitmap)
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

    private fun <os> getOs(): os? {
        var os: OutputStream? = null
        try {
            val applicationUUID = UUID.randomUUID()
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                mBluetoothSocket = mBluetooth.createRfcommSocketToServiceRecord(applicationUUID)
                mBluetoothAdapter.cancelDiscovery()
                mBluetoothSocket.connect()

                os = mBluetoothSocket.outputStream
            }
        } catch (e: IOException) {
            e.stackTrace
        } finally {
            return os as os?
        }
    }
}