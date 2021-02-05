package com.example.bluetooth.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.example.bluetooth.database.models.Level
import com.example.bluetooth.database.models.Player
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, CoroutineScope {

    private fun checkForLevels(db: AppDatabase) {
        launch {
            if (db.levelDao().getAll().isEmpty()) {
                populateLevels(db)
            }
        }
    }

    private suspend fun populateLevels(db: AppDatabase) {
        db.levelDao().insertAll(
            Level(1, 1, 5, 1), // 10 sec (dist * 10 / speed)
            Level(2, 2, 5, 3), // 15 sec
            Level(3, 2, 3, 3), // 15 sec
            Level(4, 3, 5, 6), // 20 sec
            Level(5, 3, 3, 6), // 20 sec
            Level(6, 4, 5, 8), // 20 sec
            Level(7, 4, 3, 8), // 20 sec
            Level(8, 5, 3, 10) // 20 sec
        )
    }

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }
    private lateinit var db: AppDatabase
    private val defaultChoice = getString(R.string.new_player)
    private var choice = defaultChoice

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = AppDatabase.getInstance(applicationContext)
        val playerDao = db.playerDao()

        var playersName: MutableList<String> = mutableListOf()

        launch {
            playersName = playerDao.getAllNames()
            onResult(playersName)
        }

        selectPlayerButton.setOnClickListener {
            Log.i("TAG", "le choix est : $choice ")
            if (choice == defaultChoice) {
                selectPlayerButton.visibility = View.INVISIBLE
                spinner.visibility = View.INVISIBLE
                createPlayerButton.visibility = View.VISIBLE
                createPlayerText.visibility = View.VISIBLE
            } else {
                launch {
                    changeActivity(choice)
                }
            }
        }

        createPlayerButton.setOnClickListener {
            val newPlayerName = createPlayerText.text.toString()
            if (playersName.contains(newPlayerName)) {
                Toast.makeText(
                    this,
                    getString(R.string.name_taken_text),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                launch {
                    val player = Player(newPlayerName)
                    playerDao.insertAll(player)

                    changeActivity(player.playerName)
                }
            }
        }

        checkForLevels(db)
    }

    private fun onResult(result: MutableList<String>) {
        spinner.onItemSelectedListener = this
        val spinnerAdapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, result)
        spinnerAdapter.insert(defaultChoice, 0)
        spinner.adapter = spinnerAdapter
    }

    private fun changeActivity(name: String) {
        val intent = Intent(this, SelectDeviceActivity::class.java)
        intent.putExtra("playerName", name)
        this.startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        createPlayerButton.visibility = View.INVISIBLE
        createPlayerText.visibility = View.INVISIBLE
        selectPlayerButton.visibility = View.VISIBLE
        spinner.visibility = View.VISIBLE
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        choice = parent?.getItemAtPosition(position) as String

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}