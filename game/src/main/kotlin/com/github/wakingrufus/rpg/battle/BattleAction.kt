package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.ability.Ability
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder
import com.github.wakingrufus.rpg.inventory.Consumable
import com.github.wakingrufus.rpg.inventory.removeItemFromInventory

class BattleAction(val name: String, val performer: BattleComponent, val effect: () -> Unit)

sealed class BattleActionChoice(open val name: String)

class AbilityActionChoice(override val name: String, val ability: Ability) : BattleActionChoice(name) {
    fun toBattleAction(performer: BattleComponent,
                       target: BattleComponent,
                       allies: List<BattleComponent>,
                       enemies: List<BattleComponent>): BattleAction {
        return BattleAction(name, performer) {
            ability.consumes?.let {
                removeItemFromInventory(it)
            }
            ability.performerEffect(performer)
            allies.forEach { ability.alliesEffect(it) }
            enemies.forEach { ability.enemiesEffect(it) }
            ability.targetEffect(target)
            ability.animation(performer.entity.getComponent())
        }
    }

    fun toBattleAction(performer: BattleComponent,
                       allies: List<BattleComponent>,
                       enemies: List<BattleComponent>): BattleAction {
        return BattleAction(name, performer) {
            ability.consumes?.let {
                removeItemFromInventory(it)
            }
            ability.performerEffect(performer)
            allies.forEach { ability.alliesEffect(it) }
            enemies.forEach { ability.enemiesEffect(it) }
            ability.animation(performer.entity.getComponent())
        }
    }
}

class ChooseAbilityActionChoice(override val name: String, val choices: () -> List<BattleActionChoice>) : BattleActionChoice(name)

fun chooseChoice(name: String, choices: () -> List<BattleActionChoice>): ChooseAbilityActionChoice {
    return ChooseAbilityActionChoice(name, choices)
}

fun abilityChoice(name: String, builder: AbilityBuilder.() -> Unit): AbilityActionChoice {
    return AbilityActionChoice(name = name, ability = AbilityBuilder().apply(builder).build())
}

fun consumableChoice(name: String, consumable: Consumable, builder: AbilityBuilder): AbilityActionChoice {
    return AbilityActionChoice(name = name, ability = builder.apply { consumes(consumable) }.build())
}
