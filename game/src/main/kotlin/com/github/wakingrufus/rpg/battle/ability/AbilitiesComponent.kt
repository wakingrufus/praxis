package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.github.wakingrufus.rpg.battle.BattleActionChoice
import com.github.wakingrufus.rpg.battle.HealPowerComponent
import com.github.wakingrufus.rpg.battle.action
import com.github.wakingrufus.rpg.getComponent

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
