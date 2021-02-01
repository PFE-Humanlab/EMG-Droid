package com.example.bluetooth.list_files_recycler_view

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.activity.ViewDataActivity

class ListFilesHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.elementName)

    fun bind(name: String, mContext: Context) {

        textView.text = name

        itemView.setOnClickListener {

            val intent = Intent(mContext, ViewDataActivity::class.java)
            intent.putExtra("fileName", name)
            mContext.startActivity(intent)

//            Toast.makeText(mContext, "Name : $name", Toast.LENGTH_SHORT).show()

        }
    }

}