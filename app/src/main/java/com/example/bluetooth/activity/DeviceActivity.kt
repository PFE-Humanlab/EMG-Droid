package com.example.bluetooth.activity

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.bluetooth.R
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
    var lastXValue = 0.0

    val series = LineGraphSeries<DataPoint>()

    fun updateGraphAndText(value: Int) {

        val textView = findViewById<TextView>(R.id.deviceLatestValue)
        textView.text = value.toString()

//        values.add(value)

        lastXValue += 1

        series.appendData(DataPoint(lastXValue, value.toDouble()), true, 100)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device)

        val graph = findViewById<GraphView>(R.id.graph)
        graph.addSeries(series)

        graph.viewport.isXAxisBoundsManual = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(100.0)

        graph.viewport.isYAxisBoundsManual = true
        graph.viewport.setMinY(0.0)
        graph.viewport.setMaxY(800.0)

//        Log.i("WhereAmI", "onCreate: ")

        val device = intent.getParcelableExtra<BluetoothDevice>("device")

        if (device == null) {
            // Todo : go back main menu
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {

            val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
            var bluetoothSocket: BluetoothSocket? = null

            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid)
            } catch (e: Exception) {
            }

            if (bluetoothSocket == null) {
                // Todo : go back main menu avec message erreur
                return@launch
            }

            try {
                bluetoothSocket.connect()
            } catch (connectException: IOException) {
                try {
                    bluetoothSocket.close()
                } catch (closeException: IOException) {
                }
                // Todo : go back main menu avec message erreur
                return@launch
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