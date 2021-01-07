package com.example.bluetooth.activity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bluetooth.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Thread.sleep
import java.util.*

class DeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        Log.i("WhereAmI", "onCreate: ")

        val device = intent.getParcelableExtra<BluetoothDevice>("device")

        if (device == null) {
            // Todo : go back main menu
            return
        }

        val textView = findViewById<TextView>(R.id.deviceLatestValue)

        lifecycleScope.launch(Dispatchers.IO) {

            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            var bluetoothSocket: BluetoothSocket? = null

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            } catch (e: Exception) {
            }

            if (bluetoothSocket == null) {
                // Todo : go back main menu avec message erreur
                return@launch
            }

            try {
                bluetoothSocket.connect()
            } catch (connectException: IOException) {
                try {
                    bluetoothSocket.close()
                } catch (closeException: IOException) {
                }
                // Todo : go back main menu avec message erreur
                return@launch
            }

            val inputStream = bluetoothSocket.inputStream
//            val buffer: ByteArray = ByteArray(1024)

            while (true) {
                val input = inputStream.read() * 3

                withContext(Dispatchers.Main) {
                    // update UI here
                    textView.text = input.toString()
                }
            }
        }

    }
}