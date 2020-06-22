package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet
import com.github.wakingrufus.rpg.sprites.characterSpriteSheet

class FieldLpcAnimationComponent(val spriteSheet: LPCSpriteSheet) : Component() {
    private val texture: AnimatedTexture = AnimatedTexture(spriteSheet.idleNorth)

    override fun onAdded() {
        entity.viewComponent.addChild(texture)
        texture.loopAnimationChannel(spriteSheet.idleNorth)
    }

    fun walkNorth() {
        if (texture.animationChannel != spriteSheet.walkNorth
                && texture.animationChannel != spriteSheet.walkWest
                && texture.animationChannel != spriteSheet.walkEast) {
            texture.playAnimationChannel(spriteSheet.walkNorth)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(spriteSheet.idleNorth)
            }
        }
    }

    fun walkSouth(){
        if (texture.animationChannel != spriteSheet.walkSouth
                && texture.animationChannel != spriteSheet.walkWest
                && texture.animationChannel != spriteSheet.walkEast) {
            texture.playAnimationChannel(spriteSheet.walkSouth)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(spriteSheet.idleSouth)
            }
        }
    }

    fun walkWest(){
        if (texture.animationChannel != spriteSheet.walkWest) {
            texture.playAnimationChannel(spriteSheet.walkWest)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(spriteSheet.idleWest)
            }
        }
    }

    fun walkEast(){
        if (texture.animationChannel != spriteSheet.walkEast) {
            texture.playAnimationChannel(spriteSheet.walkEast)
            texture.onCycleFinished = Runnable {
                texture.loopAnimationChannel(spriteSheet.idleEast)
            }
        }
    }
}