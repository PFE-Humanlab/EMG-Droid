package com.example.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

//         ask for Bluetooth
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null){
            Toast.makeText(this, "Cet appareil n'est pas équipé du BlueTooth", Toast.LENGTH_SHORT).show()
        }else{
            if (!bluetoothAdapter.isEnabled){
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent,1)
            }
        }

//        See paired devices

        // Todo : Screen for Select Bluetooth device
        val bluetoothDevices = bluetoothAdapter.bondedDevices
        var device : BluetoothDevice? = null
        if(bluetoothDevices.size > 0){
            bluetoothDevices.forEach {

                if (it.name == "HC-05"){
                    device = it
                }

            }
        }

        println("From main thread")

        // New thread for the communication with the arduino device

        device?.let {
            val connectionThread = ConnectionThread(it, bluetoothAdapter)
            connectionThread.start()
        }
    }
}