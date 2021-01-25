package com.example.bluetooth.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_view_data.*
import java.io.FileReader

class ViewDataActivity : AppCompatActivity() {

    private val series = LineGraphSeries<DataPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val fileNameString = intent.getStringExtra("fileName")!!

        val data: List<Int>

        openFileInput(fileNameString).use { file ->
            FileReader(file.fd).use { reader ->
                data = reader.readText().split(",").map { Integer.parseInt(it) }
            }
        }

        graphData.addSeries(series)

        val dataPoints = data.mapIndexed { index, y -> DataPoint(index.toDouble(), y.toDouble()) }

        series.resetData(dataPoints.toTypedArray())
    }
}