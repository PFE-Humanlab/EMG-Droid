package com.example.bluetooth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import com.example.bluetooth.R
import com.example.bluetooth.game.GameActivity
import com.example.bluetooth.utils.BluetoothActivity
import kotlinx.android.synthetic.main.activity_calibration.*

class CalibrationActivity : BluetoothActivity() {

    private var minValue = 1000

    private var maxValue = 0

    private var distProgress = 100
    private var speedProgress = 100
    private var delayProgress = 1000

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

        val parent = this

        distBar.apply {
            progress = distProgress
            max = 90

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    distProgress = progress + 10

                    val distV = parent.findViewById<TextView>(R.id.distValueText)
                    distV?.let {
                        it.text = distProgress.toString()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        speedBar.apply {
            progress = speedProgress
            max = 80

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    speedProgress = progress + 20

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
            progress = delayProgress
            max = 4500

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    delayProgress = progress + 500


                    val delayV = parent.findViewById<TextView>(R.id.delayValueText)
                    delayV?.let {
                        it.text = delayProgress.toString()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        startGameButton.setOnClickListener {

            val mContext = it.context

            val intent = Intent(mContext, GameActivity::class.java)

            intent.putExtra("speed", speedProgress)
            intent.putExtra("distance", distProgress)
            intent.putExtra("min", minValue)
            intent.putExtra("max", maxValue)
            intent.putExtra("delay", delayProgress)

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