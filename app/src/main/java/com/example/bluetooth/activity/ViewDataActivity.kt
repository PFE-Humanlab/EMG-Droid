package com.example.bluetooth.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.activity_view_data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ViewDataActivity : AppCompatActivity(), CoroutineScope {


    private lateinit var playerName: String
    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }

    private val series = LineGraphSeries<DataPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data)

        playerName = intent.getStringExtra("playerName") ?: return finish()

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        val measureName = intent.getStringExtra("recordName")!!

        val db = AppDatabase.getInstance(this)
        graphData.addSeries(series)

        graphData.viewport.isYAxisBoundsManual = true
        graphData.viewport.setMinY(0.0)

        launch {
            val data = db.recordDAO()
                .getRecord(measureName, playerName)
                .valuesList
                .mapIndexed { index, y -> DataPoint(index.toDouble(), y.toDouble()) }

            graphData.viewport.setMaxY(data.maxOf { it.y })

            series.resetData(data.toTypedArray())
        }
    }
}