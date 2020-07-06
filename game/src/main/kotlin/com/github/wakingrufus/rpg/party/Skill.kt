package com.github.wakingrufus.rpg.party

import com.github.wakingrufus.rpg.battle.AbilityActionChoice
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder
import com.github.wakingrufus.rpg.status.StatusEffect

class Skill(val name: String, val actions: List<AbilityActionChoice>, val statusEffects: List<StatusEffect>)

class SkillBuilder(val name: String) {
    val actions: MutableList<AbilityActionChoice> = mutableListOf()
    val statusEffects: MutableList<StatusEffect> = mutableListOf()
    fun activeAbility(name: String, builder: AbilityBuilder.() -> Unit): AbilityActionChoice {
        return AbilityActionChoice(name = name,
                ability = AbilityBuilder().apply(builder).build())
                .also {
                    actions.add(it)
                }
    }

    fun status(statusEffect: StatusEffect) {
        statusEffects.add(statusEffect)
    }

    fun build(): Skill {
        return Skill(name, actions, statusEffects)
    }
}
