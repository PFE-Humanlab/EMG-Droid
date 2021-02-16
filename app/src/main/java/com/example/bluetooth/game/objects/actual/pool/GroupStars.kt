package com.example.bluetooth.game.objects.actual.pool

import android.graphics.Bitmap
import com.example.bluetooth.game.GameLogic
import com.example.bluetooth.game.objects.abstracs.PoolGameObjects
import com.example.bluetooth.game.objects.actual.BackgroundStar
import com.example.bluetooth.game.objects.interf.Updatable
import com.example.bluetooth.utils.resizedBitmap
import com.example.bluetooth.utils.rotatedBitmap
import com.example.bluetooth.utils.uniformTransform
import kotlin.random.Random

class GroupStars(
    private val gameLogic: GameLogic,
    delayObstacle: Int,
    private val listImage: List<Bitmap>
) : PoolGameObjects<BackgroundStar>(), Updatable {

    override val list: MutableList<BackgroundStar> = mutableListOf()
    override var xDraw: Float = 0f
    override var yDraw: Float = 0f

    private val delayBetweenStars: Int = delayObstacle / 10

    private var delay: Int = 0

    init {
        val starFreq = 1000 / delayBetweenStars

        for (i in 1..(starFreq * 10)) {
            val star = createNewStar()
            star.reset(Random.nextFloat() * star.screenWidth)
        }
    }

    private fun createNewStar(): BackgroundStar {
        val image = listImage[Random.nextInt(listImage.size)]
            .rotatedBitmap(Random.nextInt(360).toFloat())

        val bitmap = image.resizedBitmap(
            (image.height * Random.nextFloat().uniformTransform(0f, 1f, 0.5f, 1f)).toInt()
        )

        val freeStar = BackgroundStar(gameLogic, bitmap)
        list.add(freeStar)

        return freeStar
    }

    override fun tickUpdate(deltaTimeMillis: Long) {
        delay -= deltaTimeMillis.toInt()

        if (delay < 0) {
            var freeStar = list.find { !it.active }

            if (freeStar == null) {
                freeStar = createNewStar()
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
