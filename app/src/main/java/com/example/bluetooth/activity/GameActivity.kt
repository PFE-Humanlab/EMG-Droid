package com.example.bluetooth.activity

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import com.example.bluetooth.R
import com.example.bluetooth.game.GameView
import com.example.bluetooth.utils.BluetoothActivity
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : BluetoothActivity() {

    lateinit var playerName: String
    private lateinit var gView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
//        will hide the title
        supportActionBar?.hide()
//        hide the title bar

        playerName = intent.getStringExtra("playerName") ?: return finish()

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController

            if (controller != null) {
                controller.hide(
                    WindowInsets.Type.statusBars() or
                            WindowInsets.Type.navigationBars()
                )
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setContentView(R.layout.activity_game)

        gView = gameView

        gView.activity = this

        gView.apply {
            // level data
            endless = intent.getBooleanExtra("endless", false)
            levelId = intent.getIntExtra("levelId", -1)

            speed = intent.getIntExtra("speed", 10)
            distance = intent.getIntExtra("distance", 10)
            delay = intent.getIntExtra("delay", 500)


            // calibr data
            minValue = intent.getIntExtra("min", 0)
            maxValue = intent.getIntExtra("max", 700)

            initGame()
        }

    }

    override fun callSuccess(value: Int) {
        gView.updatePlayer(value)
    }

    override fun callFailure() {
        finish()
    }

    override fun onBackPressed() {
        gView.stopAndJoinThread()
        super.onBackPressed()
    }

}