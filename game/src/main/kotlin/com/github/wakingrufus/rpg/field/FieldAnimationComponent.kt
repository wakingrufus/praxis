package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.github.wakingrufus.rpg.AnimationType
import com.github.wakingrufus.rpg.getAnimation
import javafx.util.Duration

class FieldAnimationComponent(name: String) : Component() {
    private val idle: AnimationChannel = getAnimation(name, AnimationType.IDLE, Duration.seconds(1.0))
    private val walking: AnimationChannel = getAnimation(name, AnimationType.WALKING, Duration.seconds(1.0))
    private val texture: AnimatedTexture = AnimatedTexture(idle)

    override fun onAdded() {
        entity.viewComponent.addChild(texture)
        texture.loopAnimationChannel(idle)
    }

    fun walk() {
        if (texture.animationChannel == idle) {
            texture.playAnimationChannel(walking)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(idle)
            }
        }
    }

    fun faceLeft() {
        entity.scaleX = -1.0
    }

    fun faceRight() {
        entity.scaleX = 1.0
    }
}