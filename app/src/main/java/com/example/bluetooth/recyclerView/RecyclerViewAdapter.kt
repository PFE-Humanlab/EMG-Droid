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

    private val deviceList = bluetoothAdapter.bondedDevices.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val device = deviceList[position]

        (holder as RecyclerViewHolder).bind(device, bluetoothAdapter, mContext)

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

}