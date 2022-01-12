package io.ramani.ramaniWarehouse.domainCore.printer

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.util.*

object BluetoothConnection {
    var instanceBluetoothAdapter: BluetoothAdapter? = null
        private set
    private val applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    var instanceBluetoothSocket: BluetoothSocket? = null
        private set
    var instanceBluetoothDevice: BluetoothDevice? = null
        private set

    fun init(deviceAddress: String?) {
        if (instanceBluetoothSocket == null) {
            initBlueToothAdapter()
            instanceBluetoothDevice = instanceBluetoothAdapter!!.getRemoteDevice(deviceAddress)
        }
    }

    fun initBluetoothSocket() {
        try {
            instanceBluetoothSocket = instanceBluetoothDevice!!.createRfcommSocketToServiceRecord(
                applicationUUID
            )
            instanceBluetoothAdapter!!.cancelDiscovery()
//            instanceBluetoothSocket.connect()
        } catch (eConnectException: IOException) {
            Log.d("Bluetooth", "CouldNotConnectToSocket", eConnectException)
            closeSocket(instanceBluetoothSocket)
            return
        }
    }

    fun initBlueToothAdapter() {
        instanceBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    private fun closeSocket(nOpenSocket: BluetoothSocket?) {
        try {
            nOpenSocket!!.close()
            Log.d("Bluetooth Singleton", "SocketClosed")
        } catch (ex: IOException) {
            Log.d("Bluetooth Singleton", "CouldNotCloseSocket")
        }
    }

    fun clearAndDisable() {
        try {
            if (instanceBluetoothSocket != null) instanceBluetoothSocket!!.close()
        } catch (e: Exception) {
            Log.e("Tag", "Exe ", e)
        }
    }
}