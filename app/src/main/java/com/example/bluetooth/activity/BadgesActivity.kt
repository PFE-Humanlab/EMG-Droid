package com.example.bluetooth.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.adapter.badgesList_recycler_view.BadgesListAdapter
import com.example.bluetooth.database.AppDatabase
import kotlinx.android.synthetic.main.activity_badges.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BadgesActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private lateinit var playerName: String
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_badges)

        playerName = intent.getStringExtra("playerName") ?: return finish()
        db = AppDatabase.getInstance(this)
        val bestScoreDAO = db.bestScoreDAO()
        val levelDAO = db.levelDAO()
        val playerDAO = db.playerDAO()

        launch {

            val player = playerDAO.getPlayerByName(playerName)

            val min = (player.bestEndless / 1000) / 60
            val sec = (player.bestEndless / 1000) % 60

            val timeHolder = getString(R.string.time_holder, min.toString(), sec.toString())

            val bestString: String = if (player.bestEndless > 0)
                getString(R.string.best_endless_time, timeHolder)
            else
                getString(R.string.not_played_endless)

            bestEndlessTextView.text = bestString

            val listBestScores = bestScoreDAO.getBestScoresByPlayerName(playerName)
            val listLevels = levelDAO.getAll()
            badgesListRV.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = BadgesListAdapter(this.context, listBestScores, listLevels)
            }
        }
    }
}
