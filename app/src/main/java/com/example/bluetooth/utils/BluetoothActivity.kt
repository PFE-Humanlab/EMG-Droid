package com.example.bluetooth.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import java.lang.Thread.sleep

abstract class BluetoothActivity : AppCompatActivity() {

    abstract fun callSuccess(value: Int)
    abstract fun callFailure()

    override fun onPause() {
        BluetoothCommunication.stopReadingData()
        BluetoothCommunication.Actor.stop()
        sleep(250)
        super.onPause()
    }

    override fun onResume() {

        // Setup data thread
        BluetoothCommunication.apply {
            setCallbackSuccess { value -> callSuccess(value) }
            setCallbackFailure { callFailure() }
            startReadingData(lifecycleScope)
        }
        BluetoothCommunication.Actor.start(lifecycleScope)
        super.onResume()
    }

}