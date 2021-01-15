package com.example.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.recyclerView.RecyclerViewAdapter

class MainActivity : AppCompatActivity() {

    private var recView: RecyclerView? = null

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val requestCodeBluetooth = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {

            Toast.makeText(
                this,
                getString(R.string.unsupported_bluetooth),
                Toast.LENGTH_SHORT
            ).show()

        } else {

            // Ask for Bluetooth
            if (!bluetoothAdapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, requestCodeBluetooth)
            }

            recView = findViewById(R.id.recyclerList)

            // Display bonded devices
            recView?.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = RecyclerViewAdapter(context, bluetoothAdapter)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == requestCodeBluetooth) {
            recView?.apply {
                adapter = bluetoothAdapter?.let { RecyclerViewAdapter(context, it) }
            }
        }
    }
}