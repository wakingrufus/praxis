package com.github.wakingrufus.rpg.battle.ability

import com.github.wakingrufus.rpg.battle.BattleComponent

class Ability(val targetEffect: (BattleComponent) -> Unit = {},
              val performerEffect: (BattleComponent) -> Unit = {},
              val enemiesEffect: (BattleComponent) -> Unit = {},
              val alliesEffect: (BattleComponent) -> Unit = {},
              val requiresTarget: Boolean = false)

class AbilityBuilder {
    var targetEffect: (BattleComponent) -> Unit = {}
    var alliesEffect: (BattleComponent) -> Unit = {}
    var enemiesEffect: (BattleComponent) -> Unit = {}
    var performerEffect: (BattleComponent) -> Unit = {}
    var requiresTarget: Boolean = false
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

    fun invoke(): Ability {
        return Ability(targetEffect = targetEffect,
                alliesEffect = alliesEffect,
                enemiesEffect = enemiesEffect,
                performerEffect = performerEffect,
                requiresTarget = requiresTarget)
    }
}

fun ability(builder: AbilityBuilder.() -> Unit): Ability {
    return AbilityBuilder().apply(builder).invoke()
}