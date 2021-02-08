package com.example.bluetooth.recycler_views.bluetooth_recycler_view

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R

class RecyclerViewAdapter(private val mContext : Context, bluetoothAdapter: BluetoothAdapter, val playerName : String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val deviceList = bluetoothAdapter.bondedDevices.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_row, parent, false)

        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val device = deviceList[position]

        (holder as RecyclerViewHolder).bind(device, playerName, mContext)

    }

    override fun getItemCount(): Int {
        return deviceList.size
    }

}