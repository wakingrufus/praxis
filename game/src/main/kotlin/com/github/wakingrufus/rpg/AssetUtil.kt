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
