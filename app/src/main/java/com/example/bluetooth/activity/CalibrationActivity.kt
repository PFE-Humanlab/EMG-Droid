package com.example.bluetooth.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.example.bluetooth.database.models.Level
import com.example.bluetooth.database.models.jointure.PlayerWithScore
import com.example.bluetooth.adapter.spinner_level_adapter.LevelArrayAdapter
import com.example.bluetooth.utils.BluetoothActivity
import kotlinx.android.synthetic.main.activity_calibration.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class CalibrationActivity : BluetoothActivity(), AdapterView.OnItemSelectedListener,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }

    private lateinit var playerName: String

    private var minValue = 1000

    private var maxValue = 0

    private var speedProgress = 1
    private var delayProgress = 1

    private lateinit var levelList: List<Level>

    private var isEndless: Boolean = false

    private fun toggleEndless(bool: Boolean) {

        isEndless = bool

        val visEndless = if (bool) View.VISIBLE else View.INVISIBLE

        speedBar.visibility = visEndless
        speedValueText.visibility = visEndless
        speedText.visibility = visEndless
        delayBar.visibility = visEndless
        delayValueText.visibility = visEndless
        delayText.visibility = visEndless

    }


    private fun updateValues(value: Int) {
        if (value < minValue) {
            minValue = value
            minText.text = minValue.toString()
        }
        if (value > maxValue) {
            maxValue = value
            maxText.text = maxValue.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calibration)

        playerName = intent.getStringExtra("playerName") ?: return finish()

        val parent = this

        val db = AppDatabase.getInstance(this)

        speedBar.apply {
            max = 9
            progress = 0

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    speedProgress = progress + 1

                    val speedV = parent.findViewById<TextView>(R.id.speedValueText)
                    speedV?.let {
                        it.text = speedProgress.toString()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        delayBar.apply {
            max = 9
            progress = 0

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    delayProgress = progress + 1


                    val delayV = parent.findViewById<TextView>(R.id.delayValueText)
                    delayV?.let {
                        it.text = delayProgress.toString()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        toggleEndless(false)
        launch {
            levelList = db.levelDAO().getAll()
            val playerWithScore = db.playerDAO().getPlayerWithScore(playerName)
            populateSpinner(playerWithScore)
        }

        startGameButton.setOnClickListener {
            val mContext = it.context
            if (isEndless) {
                changeActivity(mContext, speedProgress, -1, delayProgress)
            } else {
                changeActivity(mContext, choice.speed, choice.distance, choice.delay)
            }

        }

        badgesButton.setOnClickListener {
            val mContext = it.context

            val intent = Intent(mContext, BadgesActivity::class.java)

            intent.putExtra("playerName", playerName)
            mContext.startActivity(intent)
        }
    }

    private fun changeActivity(context: Context, speed: Int, dist: Int, delay: Int) {

        val intent = Intent(context, GameActivity::class.java)

        intent.putExtra("speed", speed * 100)

        intent.putExtra("distance", dist * 1000)
        intent.putExtra("delay", delay * 500)

        intent.putExtra("levelId", choice.levelId)

        intent.putExtra("min", minValue)
        intent.putExtra("max", maxValue)
        intent.putExtra("endless", isEndless)
        intent.putExtra("playerName", playerName)
        context.startActivity(intent)

    }

    private val levelEndless = Level(-1, 0, 0, 0)
    private var choice = levelEndless

    private fun populateSpinner(playerWithScore: PlayerWithScore) {

        selectLevelSpinner.onItemSelectedListener = this

        val newLevelId = playerWithScore.levels.maxOf { it.levelId } + 1

        val spinnerAdapter =
            LevelArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                levelList.filter { it.levelId <= newLevelId }
            )

        spinnerAdapter.add(levelEndless)

        choice = spinnerAdapter.getItem(0)!!

        selectLevelSpinner.adapter = spinnerAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        choice = parent?.getItemAtPosition(position) as Level

        toggleEndless(choice == levelEndless)

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun callSuccess(value: Int) {
        runOnUiThread {
            updateValues(value)
        }
    }

    override fun callFailure() {
        finish()
    }


}