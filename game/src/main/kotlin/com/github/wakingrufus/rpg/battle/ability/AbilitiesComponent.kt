package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.BattleActionChoice
import com.github.wakingrufus.rpg.battle.HealPowerComponent
import com.github.wakingrufus.rpg.battle.action

class AbilitiesComponent : Component() {
    private val abilities: MutableList<BattleActionChoice> = mutableListOf()
    fun addAbility(actionChoice: BattleActionChoice) {
        abilities.add(actionChoice)
    }

    fun getAbilities(): List<BattleActionChoice> {
        return abilities
    }

    fun battleActions(): List<BattleActionChoice> {
        return listOf(action("Pray") {
            alliesEffect { target -> target.heal(entity.getComponent<HealPowerComponent>().effectiveHealPower()) }
        })
    }
}
