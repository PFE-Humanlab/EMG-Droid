package com.example.bluetooth.recyclerView

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.activity.DeviceActivity

class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.elementName)

    fun bind(device: BluetoothDevice, adapter: BluetoothAdapter, context: Context){

        textView.text = device.name

        itemView.setOnClickListener {
            adapter.cancelDiscovery()

            val intent = Intent(context , DeviceActivity::class.java)
            intent.putExtra("device", device)
            context.startActivity(intent)
        }
    }

}