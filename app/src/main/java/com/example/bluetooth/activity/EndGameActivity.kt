package com.example.bluetooth.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import com.example.bluetooth.utils.leftPad
import kotlinx.android.synthetic.main.activity_end_game.*

class EndGameActivity : AppCompatActivity() {

    var finalTimeMillis: Long = 0
    var collCount: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)

        collCount = intent.getIntExtra("collision", -1)
        finalTimeMillis = intent.getLongExtra("timeMillis", 0)

        val min = (finalTimeMillis / 1000) / 60
        val sec = (finalTimeMillis / 1000) % 60

        finalTimeText.text = getString(
            R.string.time_holder,
            min.toString().leftPad(2, "0"),
            sec.toString().leftPad(2, "0")
        )

        collFinalText.text = collCount.toString()

        val endless = intent.getBooleanExtra("endless", false)

        // Todo : get bagdes if not endless, update best endless score


        // setup buttons callbacks
        goCalibrationButton.setOnClickListener {
            val mContext = it.context
            val intent = Intent(mContext, CalibrationActivity::class.java)
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

            mContext.startActivity(intent)

        }

    }
}