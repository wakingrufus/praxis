package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.almasb.fxgl.texture.AnimationChannel
import com.github.wakingrufus.rpg.AnimationType
import com.github.wakingrufus.rpg.LPCSpriteSheet
import com.github.wakingrufus.rpg.getAnimation
import javafx.util.Duration

class FieldLpcAnimationComponent(name: String) : Component() {
    private val lpc = LPCSpriteSheet(name)
    private val texture: AnimatedTexture = AnimatedTexture(lpc.idleNorth)

    override fun onAdded() {
        entity.viewComponent.addChild(texture)
        texture.loopAnimationChannel(lpc.idleNorth)
    }

    fun walkNorth() {
        if (texture.animationChannel != lpc.walkNorth
                && texture.animationChannel != lpc.walkWest
                && texture.animationChannel != lpc.walkEast) {
            texture.playAnimationChannel(lpc.walkNorth)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(lpc.idleNorth)
            }
        }
    }

    fun walkSouth(){
        if (texture.animationChannel != lpc.walkSouth
                && texture.animationChannel != lpc.walkWest
                && texture.animationChannel != lpc.walkEast) {
            texture.playAnimationChannel(lpc.walkSouth)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(lpc.idleNorth)
            }
        }
    }

    fun walkWest(){
        if (texture.animationChannel != lpc.walkWest) {
            texture.playAnimationChannel(lpc.walkWest)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(lpc.idleWest)
            }
        }
    }

    fun walkEast(){
        if (texture.animationChannel != lpc.walkEast) {
            texture.playAnimationChannel(lpc.walkEast)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(lpc.idleEast)
            }
        }
    }
}