package com.example.bluetooth.game.objects.actual

import android.graphics.Bitmap
import com.example.bluetooth.game.GameView
import com.example.bluetooth.game.objects.abstracs.PoolGameObjects
import com.example.bluetooth.game.objects.interf.Updatable
import com.example.bluetooth.utils.rotatedBitmap
import kotlin.random.Random

class GroupStars(private val gameView: GameView, private val listImage: List<Bitmap>) :
    PoolGameObjects<BackgroundStar>(), Updatable {

    override val list: MutableList<BackgroundStar> = mutableListOf()
    override var xDraw: Float = 0f
    override var yDraw: Float = 0f

    private val delayBetweenStars: Int = gameView.delay!! / 10

    private var delay: Int = 0

    override fun tickUpdate(deltaTimeMillis: Long) {
        delay -= deltaTimeMillis.toInt()

        if (delay < 0) {
            var freeStar = list.find { !it.active }

            if (freeStar == null) {
                val image = listImage
                    .get(Random.nextInt(listImage.size))
                    .rotatedBitmap(Random.nextInt(360).toFloat())

                freeStar = BackgroundStar(gameView, image)
                list.add(freeStar)
            }

            freeStar.reset()
            val randomBetween09And11 = (0.9 * Random.nextFloat() + 0.2)
            delay = (delayBetweenStars * randomBetween09And11).toInt()

        }

        list.forEach {
            it.tickUpdate(deltaTimeMillis)
        }

    }
}