package com.example.bluetooth.utils

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Thread.sleep
import java.util.*


object BluetoothCommunication {

    var device: BluetoothDevice? = null

    @Volatile
    private var readingData = true

    @Volatile
    private var callbackFailure: (() -> Unit)? = null

    @Volatile
    private var callbackSuccess: ((Int) -> Unit)? = null

    object Actor {

        @Volatile
        private var values: MutableList<Int> = mutableListOf()

        private lateinit var job: Job

        fun start(scope: LifecycleCoroutineScope) {

            job = scope.launch(Dispatchers.Default) {
                while (this.isActive) {
                    while (!values.isEmpty()) {
                        val element = values.removeAt(0)
                        withContext(Dispatchers.Main) {
                            callbackSuccess?.let { it(element) }
                        }
                    }
                    sleep(100)
                }
            }
        }

        fun stop() {
            job.cancel()
        }


        fun add(input: Int) {
            values.add(input)
        }
    }

    fun setCallbackFailure(call: () -> Unit) {
        callbackFailure = call
    }

    fun setCallbackSuccess(call: (Int) -> Unit) {
        callbackSuccess = call
    }

    fun startReadingData(scope: LifecycleCoroutineScope) {

        scope.launch(Dispatchers.IO) {

            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            var bluetoothSocket: BluetoothSocket? = null

            try {
                bluetoothSocket = device?.createRfcommSocketToServiceRecord(uuid)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (bluetoothSocket == null) {

                callbackFailure?.let { it1 -> it1() }
                return@launch
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

                callbackFailure?.let { it1 -> it1() }

                return@launch
            }

            bluetoothSocket.use {
                val inputStream = it.inputStream

                readingData = true

                while (readingData) {
                    val input = inputStream.read() * 3

                    Actor.add(input)

                }


            }


        }
    }

    fun stopReadingData() {
        readingData = false
    }


}