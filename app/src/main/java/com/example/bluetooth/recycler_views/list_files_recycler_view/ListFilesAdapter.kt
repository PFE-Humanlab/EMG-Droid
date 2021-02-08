package com.example.bluetooth.recycler_views.list_files_recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R

class ListFilesAdapter(
    private val mContext: Context,
    val playerName: String,
    private val fileList: List<String>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_row, parent, false)

        return ListFilesHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as ListFilesHolder).bind(fileList[position],playerName,  mContext)

    }

    override fun getItemCount(): Int {

        return fileList.size

    }

}