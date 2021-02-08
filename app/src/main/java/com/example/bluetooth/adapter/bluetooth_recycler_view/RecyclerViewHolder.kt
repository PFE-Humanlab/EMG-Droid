package com.example.bluetooth.adapter.bluetooth_recycler_view

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.activity.DeviceActivity

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.elementName)

    fun bind(device: BluetoothDevice, playerName: String, context: Context) {

        textView.text = device.name

        itemView.setOnClickListener {
            val intent = Intent(context, DeviceActivity::class.java)
            intent.putExtra("device", device)
            intent.putExtra("playerName", playerName)
            context.startActivity(intent)
        }
    }

}