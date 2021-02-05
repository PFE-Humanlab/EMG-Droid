package com.example.bluetooth.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.example.bluetooth.list_files_recycler_view.ListFilesAdapter
import kotlinx.android.synthetic.main.activity_list_files.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListFilesActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var playerName: String
    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_files)

        playerName = intent.getStringExtra("playerName") ?: return finish()

        val db = AppDatabase.getInstance(this)

        launch {
            listFilesView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = ListFilesAdapter(
                    context,
                    playerName,
                    db.recordDAO()
                        .getRecordsByPlayerName(playerName)
                        .map { it.recordName }
                )
            }
        }

    }
}