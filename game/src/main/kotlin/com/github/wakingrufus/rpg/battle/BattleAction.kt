package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.ability.Ability
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder
import com.github.wakingrufus.rpg.inventory.Consumable
import com.github.wakingrufus.rpg.inventory.removeItemFromInventory
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

class BattleAction(val name: String, val performer: BattleComponent, val effect: () -> Unit)

sealed class BattleActionChoice(open val name: String)

class AbilityActionChoice(override val name: String, val ability: Ability) : BattleActionChoice(name) {
    fun toBattleAction(performer: BattleComponent,
                       target: BattleComponent? = null,
                       allies: List<BattleComponent>,
                       enemies: List<BattleComponent>): BattleAction {
        return BattleAction(name, performer) {
            ability.consumes?.let {
                removeItemFromInventory(it)
            }
            ability.performerEffect(performer)
            allies.forEach { ability.alliesEffect(it) }
            enemies.forEach { ability.enemiesEffect(it) }
            target?.also { ability.targetEffect(it) }
            performer.entity.getComponent<BattleAnimationComponent>().attack(ability.animationType)
        }
    }
}

class ChooseAbilityActionChoice(override val name: String, val choices: () -> List<BattleActionChoice>) : BattleActionChoice(name)

fun chooseChoice(name: String, choices: () -> List<BattleActionChoice>): ChooseAbilityActionChoice {
    return ChooseAbilityActionChoice(name, choices)
}

fun abilityChoice(name: String,
                  attackAnimationType: AttackAnimationType,
                  builder: AbilityBuilder.() -> Unit): AbilityActionChoice {
    return AbilityActionChoice(name = name, ability = AbilityBuilder(attackAnimationType).apply(builder).build())
}

fun consumableChoice(name: String, consumable: Consumable, builder: AbilityBuilder): AbilityActionChoice {
    return AbilityActionChoice(name = name, ability = builder.apply { consumes(consumable) }.build())
}
