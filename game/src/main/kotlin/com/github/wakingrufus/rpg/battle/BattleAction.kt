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
            performer.entity.getComponent<BattleAnimationComponent>().attack(ability.animationType(performer))
            ability.performerEffect(performer)
            allies.forEach { ability.alliesEffect(performer,it) }
            enemies.forEach { ability.enemiesEffect(performer, it) }
            target?.also { ability.targetEffect(performer,it) }

        }
    }
}

class ChooseAbilityActionChoice(override val name: String, val choices: () -> List<BattleActionChoice>) : BattleActionChoice(name)

fun chooseChoice(name: String, choices: () -> List<BattleActionChoice>): ChooseAbilityActionChoice {
    return ChooseAbilityActionChoice(name, choices)
}

fun consumableChoice(name: String, consumable: Consumable, builder: AbilityBuilder): AbilityActionChoice {
    return AbilityActionChoice(name = name, ability = builder.apply { consumes(consumable) }.build())
}
