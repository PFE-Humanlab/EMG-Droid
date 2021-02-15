package com.example.bluetooth.utils

import android.bluetooth.BluetoothDevice

object BluetoothCommunication {

    private var device: BluetoothDevice? = null

    fun setDevice(dev: BluetoothDevice) {
        device = dev
    }

    private var actorThread: ActorThread? = null

    private var blueThread: BluetoothThread? = null

    fun startReadingData(failure: () -> Unit) {
        device?.let { dev ->
            actorThread?.let { act ->
                blueThread = BluetoothThread(dev, failure, act)

                blueThread?.setRunning(true)
                blueThread?.start()
            }
        }
    }

    fun stopReadingData() {

        var retry = true

        while (retry) {

            try {
                blueThread?.setRunning(false)
                blueThread?.join()
                retry = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stopActor() {
        var retry = true

        while (retry) {

            try {
                actorThread?.setRunning(false)
                actorThread?.join()
                retry = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun startActor(success: (Int) -> Unit) {
        actorThread = ActorThread(success)

        actorThread?.setRunning(true)
        actorThread?.start()
    }
}
