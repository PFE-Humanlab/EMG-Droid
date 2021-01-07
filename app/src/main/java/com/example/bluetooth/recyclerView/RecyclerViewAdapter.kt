package com.example.bluetooth.recyclerView

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R

class RecyclerViewAdapter(private val bluetoothDevices : List<BluetoothDevice>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_row, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.elementName)

        textView.text = bluetoothDevices[position].name

        holder.itemView.setOnClickListener {
            println("Pressed ${bluetoothDevices[position].name}")
        }
    }

    override fun getItemCount(): Int {
        return bluetoothDevices.size
    }

}