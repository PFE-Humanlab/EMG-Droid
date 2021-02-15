package com.example.bluetooth.utils

import androidx.appcompat.app.AppCompatActivity

abstract class BluetoothActivity : AppCompatActivity() {

    abstract fun callSuccess(value: Int)
    abstract fun callFailure()

    override fun onPause() {
        BluetoothCommunication.stopReadingData()
        BluetoothCommunication.stopActor()
        super.onPause()
    }

    override fun onResume() {

        // Setup data thread
        BluetoothCommunication.apply {
            startActor { value -> callSuccess(value) }
            startReadingData { callFailure() }
        }

        super.onResume()
    }
}
