package com.example.bluetooth.recycler_views.BadgesList_recycler_view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.database.models.Level
import kotlinx.android.synthetic.main.level_row.view.*

class BadgesListHolder(var view: View) : RecyclerView.ViewHolder(view) {



    fun bind(level: Level, bestScore: BestScore?, context : Context) {
        val medalImage : Int
        val medalText : String
        val levelId = level.levelId
        val collisions = bestScore?.collisions?:-1
        var colText = collisions.toString()
        if(collisions == -1){
            medalImage = R.drawable.no_medal
            medalText = ""
            colText = context.getString(R.string.not_played)
        }else if(collisions == 0){
            medalImage = R.drawable.gold_medal
            medalText = "Gold"
        }else if(((levelId == 1 || levelId == 2 ) && collisions <= 3 ) ||
                ((levelId == 4 || levelId == 6 || levelId == 8) && collisions <= 3 ) ||
                ((levelId == 3 || levelId == 5|| levelId == 7 ) && collisions <= 5 )){
            medalImage = R.drawable.silver_medal
            medalText = "Silver"
        }else {
            medalImage = R.drawable.bronze_medal
            medalText = "Bronze"
        }
        view.medalImageView.setImageResource(medalImage)
        view.medalTextView.text = medalText
        view.numberCollisionsTextView.text = colText

        //TODO : view.medalLevelTextView
    }

}