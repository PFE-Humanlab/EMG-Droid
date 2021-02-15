package com.example.bluetooth.adapter.spinner_level_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.bluetooth.R
import com.example.bluetooth.database.models.Level

class LevelArrayAdapter(
    private val c: Context,
    @LayoutRes resource: Int,
    private val levelList: List<Level>
) :
    ArrayAdapter<Level>(c, resource, levelList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // Todo : reuse level_row.xml

        val view2 = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.list_row_level, parent, false)

        // Endless Level ?
        val newText =
            if (levelList[position].levelId == -1)
                c.getString(R.string.endless)
            else
                c.getString(R.string.level, levelList[position].levelId.toString())

        view2.findViewById<TextView>(R.id.elementName).text = newText

        return view2
    }
}
