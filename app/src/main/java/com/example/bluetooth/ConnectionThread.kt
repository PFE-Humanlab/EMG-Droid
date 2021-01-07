package com.example.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.lang.Exception
import java.net.ConnectException
import java.util.*

class ConnectionThread(bluetoothDevice: BluetoothDevice) : Thread() {

    val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")

    lateinit var socket : BluetoothSocket

    init {
        var bluetoothSocket : BluetoothSocket? = null
        val device = bluetoothDevice

        try {
            bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
        } catch (e : Exception){    }

        bluetoothSocket?.let {
            socket = it
        }
    }

    override fun run() {
        super.run()

//        println("Trying to connect to device")

        try {
            socket.connect()
        }catch (connectException : IOException){

//            println("Error connecting to device")

            try {
                socket.close()
            }catch (closeException : IOException ){ }

            return

        }

//        println("Success connecting to device")

        val dataThread = DataThread(socket)

        dataThread.start()

    }

    override fun interrupt() {
        super.interrupt()
        try {
            socket.close()
        }catch (closeException : IOException){ }
    }

}