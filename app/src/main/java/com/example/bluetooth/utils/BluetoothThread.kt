package com.example.bluetooth.utils

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

class BluetoothThread(
    private val device: BluetoothDevice,
    val failure: () -> Unit,
    private val actor: ActorThread
) : Thread() {

    @Volatile
    private var running: Boolean = false

    fun setRunning(isRunning: Boolean) {
        synchronized(running) {
            this.running = isRunning
        }
    }

    override fun run() {

        val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        var bluetoothSocket: BluetoothSocket? = null

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (bluetoothSocket == null) {

            failure()
            return
        }

        try {
            bluetoothSocket.connect()
        } catch (connectException: IOException) {

            connectException.printStackTrace()
            try {
                bluetoothSocket.close()
            } catch (closeException: IOException) {
                closeException.printStackTrace()
            }

            failure()

            return
        }

        bluetoothSocket.use {
            val inputStream = it.inputStream

            while (running) {
                val input = inputStream.read() * 3
                actor.add(input)
            }

        }

    }
}