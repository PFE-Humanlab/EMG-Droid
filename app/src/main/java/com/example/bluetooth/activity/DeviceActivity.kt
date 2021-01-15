package com.example.bluetooth.activity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.example.bluetooth.MainActivity
import com.example.bluetooth.R
import com.example.bluetooth.game.GameActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*

class DeviceActivity : AppCompatActivity() {

    //    val values =  mutableListOf<Int>()
    private var lastXValue = 0.0

    private val series = LineGraphSeries<DataPoint>()

    private fun toMainMenu() {
        val mContext = findViewById<ConstraintLayout>(R.id.deviceLayout).context

        val intent = Intent(mContext, MainActivity::class.java)
        mContext.startActivity(intent)
    }

    private fun updateGraphAndText(value: Int) {

        val textView = findViewById<TextView>(R.id.deviceLatestValue)
        textView.text = value.toString()
        lastXValue += 1
        series.appendData(DataPoint(lastXValue, value.toDouble()), true, 100)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        // Setup start game button
        val button = findViewById<Button>(R.id.startGameButton)
        button.setOnClickListener {
            val mContext = button.context
            val intent = Intent(mContext, GameActivity::class.java)
            mContext.startActivity(intent)
        }

        // Setup graph
        val graph = findViewById<GraphView>(R.id.graph)
        graph.addSeries(series)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(100.0)

        // Setup data thread
        val device = intent.getParcelableExtra<BluetoothDevice>("device") ?: return toMainMenu()

        lifecycleScope.launch(Dispatchers.IO) {

            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            var bluetoothSocket: BluetoothSocket? = null

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            } catch (e: Exception) {
            }

            if (bluetoothSocket == null) {
                return@launch toMainMenu()
            }

            try {
                bluetoothSocket.connect()
            } catch (connectException: IOException) {
                try {
                    bluetoothSocket.close()
                } catch (closeException: IOException) {
                }
                return@launch toMainMenu()
            }

            val inputStream = bluetoothSocket.inputStream
//            val buffer: ByteArray = ByteArray(1024)

            while (true) {
                val input = inputStream.read() * 3

                withContext(Dispatchers.Main) {
                    // update UI here
                    updateGraphAndText(input)
                }
            }
        }

    }
}