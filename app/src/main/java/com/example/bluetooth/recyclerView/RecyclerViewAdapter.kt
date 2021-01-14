package com.example.bluetooth.recyclerView

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.activity.DeviceActivity

class RecyclerViewAdapter(private val mContext : Context, private val bluetoothAdapter: BluetoothAdapter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val deviceList = bluetoothAdapter.bondedDevices.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.elementName)

        val device = deviceList[position]

        textView.text = device.name

        holder.itemView.setOnClickListener {

            bluetoothAdapter.cancelDiscovery()

            val intent = Intent(mContext , DeviceActivity::class.java)
            intent.putExtra("device", device)
            mContext.startActivity(intent)


        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

}