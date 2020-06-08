package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.github.wakingrufus.rpg.AnimationType
import com.github.wakingrufus.rpg.getAnimation
import javafx.util.Duration

enum class Orientation {
    LEFT, RIGHT
}

class BattleAnimationComponent(name: String, val orientation: Orientation) : Component() {
    private val idle: AnimationChannel = getAnimation(name, AnimationType.IDLE, Duration.seconds(1.0))
    private val attack: AnimationChannel = getAnimation(name, AnimationType.ATTACKING, Duration.seconds(1.0))
    private val texture: AnimatedTexture = AnimatedTexture(idle)

    override fun onAdded() {
        if (orientation == Orientation.LEFT) {
            getEntity().scaleX = -1.0
        }
        entity.viewComponent.addChild(texture)
    }

    fun attack() {
        texture.playAnimationChannel(attack)
        texture.onCycleFinished = Runnable {
            texture.loopAnimationChannel(idle)
        }
    }
}