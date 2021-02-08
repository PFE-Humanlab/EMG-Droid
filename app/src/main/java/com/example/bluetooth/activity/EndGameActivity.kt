package com.example.bluetooth.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.example.bluetooth.database.models.BestScore
import com.example.bluetooth.utils.leftPad
import kotlinx.android.synthetic.main.activity_end_game.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EndGameActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        val collCount = intent.getIntExtra("collision", -1)
        val finalTimeMillis = intent.getLongExtra("timeMillis", -1)
        val levelId = intent.getIntExtra("levelId", -1)

        val min = (finalTimeMillis / 1000) / 60
        val sec = (finalTimeMillis / 1000) % 60

        finalTimeText.text = getString(
            R.string.time_holder,
            min.toString().leftPad(2, "0"),
            sec.toString().leftPad(2, "0")
        )

        collFinalText.text = collCount.toString()

        val endless = intent.getBooleanExtra("endless", false)

        val playerName = intent.getStringExtra("playerName") ?: return finish()

        // get bestScore
        val db = AppDatabase.getInstance(this)

        // update visibility
        if (endless) {
            endBadgeImageView.visibility = View.INVISIBLE
            endBadgeTextView.visibility = View.INVISIBLE
        } else {
            bestTimeTextView.visibility = View.INVISIBLE
            bestTimeValueTextView.visibility = View.INVISIBLE
        }

        launch {
            // calculate medal
            if (endless) {
                val playerDAO = db.playerDAO()
                val player = playerDAO.getPlayerByName(playerName)

                if (player.bestEndless < finalTimeMillis) {
                    player.bestEndless = finalTimeMillis
                    playerDAO.updatePlayers(player)
                }

                bestTimeValueTextView.text = player.bestEndless.toString()

            } else {

                val bestScoreDAO = db.bestScoreDAO()
                val bestScore = bestScoreDAO.getBestScore(playerName, levelId)

                if (bestScore == null) {
                    val bestScores = BestScore(playerName, levelId)
                    bestScores.collisions = collCount
                    bestScoreDAO.insertAll(bestScores)
                } else {

                    if (collCount < bestScore.collisions) {
                        bestScore.collisions = collCount
                        bestScoreDAO.updateBestScore(bestScore)
                    }

                }

                val level = db.levelDAO().getById(levelId)

                val medalImage: Int

                val medalText: String
                if (collCount == 0) { // gold
                    medalImage = R.drawable.gold_medal
                    medalText = "Gold"
                } else if (collCount < level.threshold) { // silver
                    medalImage = R.drawable.silver_medal
                    medalText = "Silver"
                } else { // bronze
                    medalImage = R.drawable.bronze_medal
                    medalText = "Bronze"
                }

                endBadgeImageView.setImageResource(medalImage)
                endBadgeTextView.text = medalText

            }
        }


        // setup buttons callbacks
        goCalibrationButton.setOnClickListener {
            val mContext = it.context
            val intent = Intent(mContext, CalibrationActivity::class.java)
            intent.putExtra("playerName", playerName)
            mContext.startActivity(intent)
        }


        tryAgainButton.setOnClickListener {

            val speed = intent.getIntExtra("speed", 10)
            val distance = intent.getIntExtra("distance", 10)
            val delay = intent.getIntExtra("delay", 500)
            val minValue = intent.getIntExtra("min", 0)
            val maxValue = intent.getIntExtra("max", 700)

            val mContext = it.context

            val intent = Intent(mContext, GameActivity::class.java)

            intent.putExtra("speed", speed)
            intent.putExtra("distance", distance)
            intent.putExtra("delay", delay)
            intent.putExtra("min", minValue)
            intent.putExtra("max", maxValue)
            intent.putExtra("endless", endless)

            // Meta infos
            intent.putExtra("playerName", playerName)
            intent.putExtra("levelId", levelId)

            mContext.startActivity(intent)

        }

    }
}