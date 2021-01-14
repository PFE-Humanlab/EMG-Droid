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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recView = findViewById<RecyclerView>(R.id.recyclerList)

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Cet appareil n'est pas équipé du BlueTooth", Toast.LENGTH_SHORT)
                .show()
        } else {

            // Demande activation du Bluetooth
            if (!bluetoothAdapter.isEnabled) {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, 1)
            }

            // Affiche les appareils appairée
            recView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = RecyclerViewAdapter(context, bluetoothAdapter)
            }

        }
    }
}