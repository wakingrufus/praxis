package com.github.wakingrufus.rpg.battle.ability

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.BattleAnimationComponent
import com.github.wakingrufus.rpg.battle.BattleComponent
import com.github.wakingrufus.rpg.inventory.Consumable

class Ability(val targetEffect: (BattleComponent) -> Unit = {},
              val performerEffect: (BattleComponent) -> Unit = {},
              val enemiesEffect: (BattleComponent) -> Unit = {},
              val alliesEffect: (BattleComponent) -> Unit = {},
              val requiresTarget: Boolean = false,
              val consumes: Consumable?,
              val animation: BattleAnimationComponent.() -> Unit = {})

@RpgDsl
class AbilityBuilder {
    private var targetEffect: (BattleComponent) -> Unit = {}
    private var alliesEffect: (BattleComponent) -> Unit = {}
    private var enemiesEffect: (BattleComponent) -> Unit = {}
    private var performerEffect: (BattleComponent) -> Unit = {}
    private var requiresTarget: Boolean = false
    private var consumes: Consumable? = null
    private var animation: BattleAnimationComponent.() -> Unit = {}
    fun consumes(consumable: Consumable) {
        consumes = consumable
    }

    fun targetEffect(effect: (BattleComponent) -> Unit) {
        targetEffect = effect
        requiresTarget = true
    }

    fun performerEffect(effect: (BattleComponent) -> Unit) {
        performerEffect = effect
    }

    fun alliesEffect(effect: (BattleComponent) -> Unit) {
        alliesEffect = effect
    }

    fun enemiesEffect(effect: (BattleComponent) -> Unit) {
        enemiesEffect = effect
    }
    fun animation(builder:  BattleAnimationComponent.() -> Unit){
        this.animation = builder
    }

    fun build(): Ability {
        return Ability(targetEffect = targetEffect,
                alliesEffect = alliesEffect,
                enemiesEffect = enemiesEffect,
                performerEffect = performerEffect,
                requiresTarget = requiresTarget,
                consumes = consumes,
        animation = animation)
    }
}

fun ability(builder: AbilityBuilder.() -> Unit): Ability {
    return AbilityBuilder().apply(builder).build()
}