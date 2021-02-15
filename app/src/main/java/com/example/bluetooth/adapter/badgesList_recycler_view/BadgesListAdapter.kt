package com.example.bluetooth.adapter.badgesList_recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.database.models.Level

class BadgesListAdapter(private val mContext: Context, private val bestScoreList: List<BestScore>, private val listLevels: List<Level>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.level_row, parent, false)
        return BadgesListHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bestScore = bestScoreList.find {
            it.levelId == listLevels[position].levelId
        }

        (holder as BadgesListHolder).bind(listLevels[position], bestScore, mContext)
    }

    override fun getItemCount(): Int {
        return listLevels.size
    }
}
