package com.example.bluetooth.activity

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import com.example.bluetooth.R
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

            max = 9
            progress = 0

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    distProgress = progress + 1

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

        startGameButton.setOnClickListener {

            val mContext = it.context

            val intent = Intent(mContext, GameActivity::class.java)

            intent.putExtra("speed", speedProgress * 100)
            intent.putExtra("distance", distProgress * 10000)
            intent.putExtra("min", minValue)
            intent.putExtra("max", maxValue)
            intent.putExtra("delay", delayProgress * 500)
            intent.putExtra("endless", false)

            mContext.startActivity(intent)
        }
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