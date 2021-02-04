package com.example.bluetooth.activity

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.example.bluetooth.R
import com.example.bluetooth.utils.BluetoothActivity
import com.example.bluetooth.utils.BluetoothCommunication
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_device.*
import java.io.FileWriter

class DeviceActivity : BluetoothActivity() {

    private lateinit var device: BluetoothDevice
    private var lastXValue = 0
    private var maxY = 100
    private var maxDataPoints = 100

    private var isRecording = false

    private val series = LineGraphSeries<DataPoint>()

    private val values = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        device = intent.getParcelableExtra("device") ?: return finish()

        BluetoothCommunication.setDevice(device)

        // Setup start game button
        startCalibrButton.setOnClickListener {
            val mContext = it.context
            val intent = Intent(mContext, CalibrationActivity::class.java)
            mContext.startActivity(intent)
        }

        fileName.setText(System.currentTimeMillis().toString())
        startButton.text = getString(R.string.start_recording)

        startButton.setOnClickListener {
            if (isRecording) {

                stopRecording()

            } else {

                // si champ vide prend timestamp
                if (fileName.text == null || fileName.text.toString() == "") {
                    fileName.setText(System.currentTimeMillis().toString())
                }

                val fileNameString = fileName.text.toString()

                // si existe, demande si ecrase :

                val fileList = fileList()
                if (fileList.contains(fileNameString)) {

                    val alertDialog = AlertDialog.Builder(it.context)
                        .setMessage(getString(R.string.overwrite_warning))
                        .setPositiveButton(getString(R.string.yes)) { _: DialogInterface, _: Int ->
                            startRecording()
                        }
                        .setNegativeButton(getString(R.string.no)) { _: DialogInterface, _: Int ->
                        }
                    alertDialog.show()

                } else {
                    startRecording()
                }
            }
        }

        // Setup graph
        graph.addSeries(series)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(100.0)

        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(0.0)
        graph.viewport.setMaxY(maxY.toDouble())

    }

    private fun startRecording() {
        isRecording = true
        startButton.text = getString(R.string.stop_recording)
        fileName.isEnabled = false
    }

    private fun stopRecording() {
        isRecording = false

        startButton.text = getString(R.string.start_recording)
        fileName.isEnabled = true

        // save to text file
        val fileNameString = fileName.text.toString()

        openFileOutput(fileNameString, Context.MODE_PRIVATE).use { stream ->
            FileWriter(stream.fd).use {
                val data = values.joinToString(",")
                it.write(data)
            }
        }

        values.removeAll { true }

    }


    private fun updateGraphAndText(value: Int) {
        deviceLatestValue.text = value.toString()
        lastXValue += 1

        // Update Stored List
        if (isRecording) {
            values.add(value)
        }

        // update display
        series.appendData(
            DataPoint(lastXValue.toDouble(), value.toDouble()),
            true,
            maxDataPoints
        )

        // TODO : update max Y by last $maxDataPoints values
        if (value > maxY) {
            maxY = value
            graph.viewport.setMaxY(maxY.toDouble())
        }


    }

    override fun callSuccess(value: Int) {
        runOnUiThread {
            updateGraphAndText(value)
        }
    }

    override fun callFailure() {
        finish()
    }

}