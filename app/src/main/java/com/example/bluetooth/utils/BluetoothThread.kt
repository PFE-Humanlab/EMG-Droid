package com.example.bluetooth.utils

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID

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

        bluetoothSocket.use { socket ->
            val buffer = ByteArray(1000)
            val inputStream = socket.inputStream
            var inputString = ""

            while (running) {
                val size = inputStream.read(buffer)

                if (size > 0) {

                    val bufferString = buffer.decodeToString(0, size)

                    bufferString.forEach {

                        if (it == ';') {

                            try {
                                val input = Integer.parseInt(inputString)
                                actor.add(input)
                            } catch (e: NumberFormatException) {
                                e.printStackTrace()
                            }

                            inputString = ""
                        } else {
                            inputString += it
                        }
                    }
                }
            }
        }
    }
}
