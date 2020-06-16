package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.github.wakingrufus.rpg.LPCSpriteSheet

enum class Orientation {
    LEFT, RIGHT
}

class BattleAnimationComponent(name: String, var orientation: Orientation) : Component() {
    val spriteSheet = LPCSpriteSheet(name)
    private val texture: AnimatedTexture = AnimatedTexture(spriteSheet.idleNorth)

    override fun onAdded() {
        if (orientation == Orientation.LEFT) {
            texture.playAnimationChannel(spriteSheet.idleNorth)
        } else {
            texture.playAnimationChannel(spriteSheet.idleNorth)
        }
        entity.viewComponent.addChild(texture)
    }

    fun attack() {
        if (orientation == Orientation.LEFT) {
            texture.playAnimationChannel(spriteSheet.attackLeft)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(spriteSheet.idleWest)
            }
        } else {
            texture.playAnimationChannel(spriteSheet.attackRight)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(spriteSheet.idleEast)
            }
        }
    }
}
