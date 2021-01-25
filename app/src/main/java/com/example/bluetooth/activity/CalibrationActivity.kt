package com.example.bluetooth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.example.bluetooth.R
import com.example.bluetooth.game.GameActivity
import com.example.bluetooth.utils.BluetoothActivity
import kotlinx.android.synthetic.main.activity_calibration.*

class CalibrationActivity : BluetoothActivity() {

    private var minValue = 1000

    private var maxValue = 0

    private var distProgress = 100
    private var speedProgress = 100

    fun updateValues(value: Int) {
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

        distBar.apply {
            progress = distProgress
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    distProgress = progress
                    distValue.text = distProgress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        speedBar.apply {
            progress = speedProgress
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    speedProgress = progress
                    speedValue.text = speedProgress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        startGameButton.setOnClickListener {

            val mContext = startCalibrButton.context

            val intent = Intent(mContext, GameActivity::class.java)

            intent.putExtra("speed", speedProgress)
            intent.putExtra("distance", distProgress)
            intent.putExtra("min", minValue)
            intent.putExtra("max", maxValue)

            mContext.startActivity(intent)
        }
    }

    override fun callSuccess(value: Int) {
        updateValues(value)
    }

    override fun callFailure() {
        finish()
    }

}