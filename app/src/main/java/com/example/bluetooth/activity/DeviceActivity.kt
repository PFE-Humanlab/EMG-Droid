package com.example.bluetooth.activity

import android.app.AlertDialog
import android.bluetooth.BluetoothDevice
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.example.bluetooth.database.models.Record
import com.example.bluetooth.utils.BluetoothActivity
import com.example.bluetooth.utils.BluetoothCommunication
import com.example.bluetooth.utils.toSimpleString
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_device.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.coroutines.CoroutineContext

class DeviceActivity : BluetoothActivity(), CoroutineScope {

    private lateinit var db: AppDatabase
    private lateinit var device: BluetoothDevice
    private lateinit var playerName: String

    private var lastX = 0
    private var maxY = 100
    private var maxDataPoints = 200

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }

    private var isRecording = false

    private val series = LineGraphSeries<DataPoint>()

    private val values = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        device = intent.getParcelableExtra("device") ?: return finish()
        playerName = intent.getStringExtra("playerName") ?: return finish()

        BluetoothCommunication.setDevice(device)

        // Setup start game button
        startCalibrButton.setOnClickListener {
            val mContext = it.context
            val intent = Intent(mContext, CalibrationActivity::class.java)
            intent.putExtra("playerName", playerName)
            mContext.startActivity(intent)
        }

        measureName.setText(Date().toSimpleString())
        startButton.text = getString(R.string.start_recording)

        db = AppDatabase.getInstance(this)

        startButton.setOnClickListener {

            if (isRecording) {
                stopRecording()
            } else {
                // si champ vide prend timestamp
                if (measureName.text == null || measureName.text.toString() == "") {
                    measureName.setText(Date().toSimpleString())
                }

                val recordName = measureName.text.toString()

                // si existe, demande si ecrase :

                launch {

                    val listNames = db.recordDAO()
                        .getRecordsByPlayerName(playerName)
                        .map { it.recordName }

                    if (listNames.contains(recordName)) {
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
        measureName.isEnabled = false

        startCalibrButton.isEnabled = false
    }

    private fun stopRecording() {
        isRecording = false

        startButton.text = getString(R.string.start_recording)
        measureName.isEnabled = true
        startCalibrButton.isEnabled = true

        // save to text file
        val measureName = measureName.text.toString()

        launch {
            db.recordDAO().insertAll(Record(measureName, playerName, values))
            values.removeAll { true }
        }
    }

    private fun updateGraphAndText(value: Int) {
        deviceLatestValue.text = value.toString()
        lastX += 1

        // Update Stored List
        if (isRecording) {
            values.add(value)
        }

        // update display
        series.appendData(
            DataPoint(lastX.toDouble(), value.toDouble()),
            true,
            maxDataPoints
        )

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
