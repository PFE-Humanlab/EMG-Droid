package com.example.bluetooth.recycler_views.BadgesList_recycler_view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.database.models.Level
import com.example.bluetooth.recycler_views.bluetooth_recycler_view.RecyclerViewHolder

class BadgesListAdapter(private val mContext : Context, val bestScoreList : List<BestScore>, val listLevels : List<Level>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.member_row, parent, false)
        return RecyclerViewHolder(view)
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