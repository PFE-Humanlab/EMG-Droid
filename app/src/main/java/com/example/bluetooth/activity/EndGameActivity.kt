package com.example.bluetooth.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGameActivity : AppCompatActivity() {

    var finalTimeMillis : Long = 0
    var collCount : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        collCount = intent.getIntExtra("collision", -1)
        finalTimeMillis = intent.getLongExtra("timeMillis", 0)

        val min = (finalTimeMillis / 1000) / 60
        val sec = (finalTimeMillis / 1000) % 60

        finalTimeText.text = getString(R.string.time_holder, min.toString(), sec.toString())

        collFinalText.text = collCount.toString()

        goCalibrationButton.setOnClickListener {

        }

        tryAgainButton.setOnClickListener {

        }

    }
}