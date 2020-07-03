package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.texture.AnimatedTexture
import com.github.wakingrufus.rpg.sprites.AttackAnimationType
import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet
import com.github.wakingrufus.rpg.sprites.SpriteOrientation

class BattleAnimationComponent(val spriteSheet: LPCSpriteSheet, var orientation: SpriteOrientation) : Component() {

    private val texture: AnimatedTexture = AnimatedTexture(spriteSheet.idleNorth)

    override fun onAdded() {
        texture.playAnimationChannel(spriteSheet.getIdleAnimation(orientation))
        entity.viewComponent.addChild(texture)
    }

    fun attack(type: AttackAnimationType) {
        texture.playAnimationChannel(spriteSheet.getAttackAnimation(type, orientation))
        texture.onCycleFinished = Runnable {
            texture.loopAnimationChannel(spriteSheet.getIdleAnimation(orientation))
        }
    }

    fun die(){
        texture.playAnimationChannel(spriteSheet.die)
    }
}
