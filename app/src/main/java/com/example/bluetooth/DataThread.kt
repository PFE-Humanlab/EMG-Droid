package com.example.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.BufferedInputStream
import java.io.IOException
//import java.io.IOException
import java.io.InputStream

class DataThread(bluetoothSocket: BluetoothSocket) : Thread() {

    var inputStream: InputStream
    private val buffer: ByteArray = ByteArray(1024)

    init {
        inputStream = bluetoothSocket.inputStream
    }

    override fun run() {
        super.run()

        while (true) {

            val nbBytes = try{
                inputStream.read(buffer)
            } catch(e: IOException){
                Log.d("READ_TAG", "IOException")
            }

            println("nbBytes : $nbBytes")
            for (i in 0 until nbBytes) {
                println("Byte $i: ${buffer[i].toInt()}")
            }

            // sleep(250)
        }

    }

}