package com.github.wakingrufus.rpg.battle.ability

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.BattleComponent
import com.github.wakingrufus.rpg.inventory.Consumable
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

class Ability(val targetEffect: (BattleComponent, BattleComponent) -> Unit = { _, _ -> },
              val performerEffect: (BattleComponent) -> Unit = { },
              val enemiesEffect: (BattleComponent, BattleComponent) -> Unit = { _, _ -> },
              val alliesEffect: (BattleComponent, BattleComponent) -> Unit = { _, _ -> },
              val requiresTarget: Boolean = false,
              val consumes: Consumable?,
              val animationType: (BattleComponent) -> AttackAnimationType)

@RpgDsl
class AbilityBuilder {
    private var targetEffect: (BattleComponent, BattleComponent) -> Unit = { _, _ -> }
    private var alliesEffect: (BattleComponent, BattleComponent) -> Unit = { _, _ -> }
    private var enemiesEffect: (BattleComponent, BattleComponent) -> Unit = { _, _ -> }
    private var performerEffect: (BattleComponent) -> Unit = { }
    private var animation: (BattleComponent) -> AttackAnimationType = { it.attackAnimationType() }
    private var requiresTarget: Boolean = false
    private var consumes: Consumable? = null

    fun consumes(consumable: Consumable) {
        consumes = consumable
    }

    fun animation(animation: (BattleComponent) -> AttackAnimationType) {
        this.animation = animation
    }

    fun targetEffect(effect: (BattleComponent, BattleComponent) -> Unit) {
        targetEffect = effect
        requiresTarget = true
    }

    fun performerEffect(effect: (BattleComponent) -> Unit) {
        performerEffect = effect
    }

    fun alliesEffect(effect: (BattleComponent, BattleComponent) -> Unit) {
        alliesEffect = effect
    }

    fun enemiesEffect(effect: (BattleComponent, BattleComponent) -> Unit) {
        enemiesEffect = effect
    }

    fun build(): Ability {
        return Ability(targetEffect = targetEffect,
                alliesEffect = alliesEffect,
                enemiesEffect = enemiesEffect,
                performerEffect = performerEffect,
                requiresTarget = requiresTarget,
                consumes = consumes,
                animationType = animation)
    }
}
