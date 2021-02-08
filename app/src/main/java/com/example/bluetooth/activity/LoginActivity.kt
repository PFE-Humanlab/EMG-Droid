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
            if (db.levelDAO().getAll().isEmpty()) {
                populateLevels(db)
            }
        }
    }

    private suspend fun populateLevels(db: AppDatabase) {
        db.levelDAO().insertAll(
            Level(1, 2, 5, 2), // 10 sec (dist * 10 / speed), 4 obst ( duration / (delay / 2))
            Level(2, 3, 5, 4), // 15 sec, 6 obst
            Level(3, 3, 3, 4), // 15 sec, 10 o
            Level(4, 4, 5, 8), // 20 sec, 8 o
            Level(5, 4, 3, 8), // 20 sec, 13 o
            Level(6, 5, 5, 10), // 20 sec, 8 o
            Level(7, 5, 3, 10), // 20 sec, 13 o
            Level(8, 6, 3, 12) // 20 sec,8 o
        )
    }

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }

    private lateinit var db: AppDatabase

    private lateinit var defaultChoice: String
    private lateinit var choice: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        defaultChoice = getString(R.string.new_player)
        choice = defaultChoice

        db = AppDatabase.getInstance(applicationContext)
        val playerDao = db.playerDAO()

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
    }
}