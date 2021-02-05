package com.example.bluetooth.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.bluetooth.R
import com.example.bluetooth.database.AppDatabase
import com.example.bluetooth.database.models.Player
import com.example.bluetooth.database.models.ViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginActivity() : AppCompatActivity(), AdapterView.OnItemSelectedListener,CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() {
            return Dispatchers.Main
        }
    private lateinit var db: AppDatabase
    private var choice = "New Player"
    val model : ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = AppDatabase.getInstance(applicationContext)
        val playerDao = db.playerDao()

        var playersName : MutableList<String> = mutableListOf()
        launch{
            playersName = playerDao.getAllNames()
            onResult(playersName)
        }
        selectPlayerButton.setOnClickListener {
            Log.i("TAG", "le choix est : $choice ")
            if (choice.equals("New Player")){
                selectPlayerButton.visibility = View.INVISIBLE
                spinner.visibility = View.INVISIBLE
                createPlayerButton.visibility = View.VISIBLE
                createPlayerText.visibility = View.VISIBLE
            }else{
                launch {
                    model.player = playerDao.getPlayerByName(choice)
                    changeActivity()
                }
            }
        }
        createPlayerButton.setOnClickListener {
            val newPlayerName = createPlayerText.text.toString()
            if (playersName.contains(newPlayerName)){
                Toast.makeText(this, "This name is already taken. Please choose another one.", Toast.LENGTH_SHORT).show()
            }else{
                launch {
                    val player = Player(newPlayerName)
                    playerDao.insertAll(player)
                    model.player = player
                    changeActivity()
                }
            }
        }

    }

    fun onResult(result: MutableList<String>){
        spinner.onItemSelectedListener = this
        val spinnerAdapter = ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,result)
        spinnerAdapter.insert("New Player",0)
        spinner.adapter = spinnerAdapter
    }

    fun changeActivity(){
        val intent = Intent(this,MainActivity::class.java)
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