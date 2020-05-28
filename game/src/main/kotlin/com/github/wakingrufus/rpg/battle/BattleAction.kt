package com.github.wakingrufus.rpg.battle

import com.github.wakingrufus.rpg.battle.ability.Ability
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder

class BattleAction(val name: String, val performer: BattleComponent, val effect: () -> Unit)
class BattleActionChoice(val name: String, val ability: Ability) {
    fun toBattleAction(performer: BattleComponent,
                       target: BattleComponent,
                       allies: List<BattleComponent>,
                       enemies: List<BattleComponent>): BattleAction {
        return BattleAction(name, performer) {
            ability.performerEffect(performer)
            allies.forEach { ability.alliesEffect(it) }
            enemies.forEach { ability.enemiesEffect(it) }
            ability.targetEffect(target)
        }
    }

    fun toBattleAction(performer: BattleComponent,
                       allies: List<BattleComponent>,
                       enemies: List<BattleComponent>): BattleAction {
        return BattleAction(name, performer) {
            ability.performerEffect(performer)
            allies.forEach { ability.alliesEffect(it) }
            enemies.forEach { ability.enemiesEffect(it) }
        }
    }
}

fun action(name: String, builder: AbilityBuilder.() -> Unit): BattleActionChoice {
    return BattleActionChoice(name = name, ability = AbilityBuilder().apply(builder).invoke())
}
