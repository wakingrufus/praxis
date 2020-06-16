package com.github.wakingrufus.rpg

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.fxgl.texture.getDummyImage
import javafx.scene.image.Image
import javafx.util.Duration

enum class AnimationType(val path: String) {
    ATTACKING("Attacking"),
    IDLE("Idle"),
    WALKING("Walking")
}

fun getSprite(name: String, type: AnimationType, frame: Int): String {
    return "$name/${type.path}/${name}_${type.path}_${frame.toString().padStart(3, '0')}.png"
}

fun getAnimation(name: String, type: AnimationType, duration: Duration): AnimationChannel {
    val list: MutableList<Image> = mutableListOf()
    var index = 0
    var nextImage = FXGL.image(getSprite(name, type, index))
    while (nextImage != getDummyImage()) {
        list.add(nextImage)
        index++
        nextImage = FXGL.image(getSprite(name, type, index))
    }
    return AnimationChannel(list, duration)
}

class LPCSpriteSheet(val name: String) {
    private val image = FXGL.image("$name.png")
    val spellCastNorth = of(0, 7)
    val attackLeft = of(5, 8)
    val attackRight = of(7, 8)
    val walkNorth = of(8, 9)

    //   val walkNorth = AnimationChannel(FXGL.image(name),13,64,64, Duration.seconds(1.0),104,112)
    val idleNorth = AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 104, 104)
    val walkWest = AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 117, 125)
    val idleWest = AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 117, 117)
    val walkSouth = AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 130, 138)

    val walkEast = AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 143, 151)
    val idleEast = of(11, 1)
    private fun of(row: Int, frames: Int): AnimationChannel {
        return AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 13 * row, (13 * row) + frames - 1)
    }
}