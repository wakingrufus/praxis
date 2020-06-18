package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.battle.BattleActionChoice
import com.github.wakingrufus.rpg.battle.abilityChoice
import com.github.wakingrufus.rpg.battle.battleComponent

@Required(EquipmentComponent::class)
class AbilitiesComponent : Component() {
    private val log = Logger.get(javaClass)
    private val abilities: MutableList<BattleActionChoice> = mutableListOf()
    fun addAbility(actionChoice: BattleActionChoice) {
        abilities.add(actionChoice)
    }

    fun getAbilities(): List<BattleActionChoice> {
        return abilities
    }

    override fun onAdded() {
        addAbility(
                abilityChoice("Attack") {
                    targetEffect { target ->
                        val attackType = entity.getComponent<EquipmentComponent>().attackDamageType()
                        val attackPower = entity.getComponent<EquipmentComponent>().attackModifier(attackType)
                        log.info("${entity.battleComponent().name} attacks for $attackPower $attackType damage")
                        target.takeDamage(attackPower, attackType)
                    }
                    animation { attack() }
                })
    }
}
