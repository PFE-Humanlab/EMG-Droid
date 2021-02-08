package com.example.bluetooth.adapter.BadgesList_recycler_view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.database.models.Level
import kotlinx.android.synthetic.main.level_row.view.*

class BadgesListHolder(var view: View) : RecyclerView.ViewHolder(view) {


    fun bind(level: Level, bestScore: BestScore?, context: Context) {

        val medalImage: Int
        val medalText: String
        val collisions = bestScore?.collisions ?: -1
        var colText = collisions.toString()

        if (collisions == -1) {
            medalImage = R.drawable.no_medal
            medalText = ""
            colText = context.getString(R.string.not_played)
        } else if (collisions == 0) {
            medalImage = R.drawable.gold_medal
            medalText = "Gold"
        } else if (collisions <= level.threshold) {
            medalImage = R.drawable.silver_medal
            medalText = "Silver"
        } else {
            medalImage = R.drawable.bronze_medal
            medalText = "Bronze"
        }

        view.medalImageView.setImageResource(medalImage)
        view.medalTextView.text = medalText
        view.numberCollisionsTextView.text = colText

        view.levelTextView.text = context.getString(R.string.level, level.levelId.toString())

    }

}