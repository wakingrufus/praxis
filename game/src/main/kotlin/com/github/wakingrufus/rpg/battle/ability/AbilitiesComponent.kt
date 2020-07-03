package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.github.wakingrufus.rpg.battle.BattleActionChoice

class AbilitiesComponent(initialAbilities: List<BattleActionChoice>) : Component() {
    private val abilities: MutableList<BattleActionChoice> = initialAbilities.toMutableList()
    fun addAbility(actionChoice: BattleActionChoice) {
        abilities.add(actionChoice)
    }

    fun getAbilities(): List<BattleActionChoice> {
        return abilities
    }
}
