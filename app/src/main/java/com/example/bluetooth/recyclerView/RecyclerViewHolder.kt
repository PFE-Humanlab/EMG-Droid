package com.example.bluetooth.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R

class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val textView = view.findViewById<TextView>(R.id.elementName)
}