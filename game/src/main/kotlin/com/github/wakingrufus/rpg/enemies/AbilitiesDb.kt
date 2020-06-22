package com.github.wakingrufus.rpg.enemies

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.AbilityActionChoice
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

@RpgDsl
open class AbilitiesDb {
    fun ability(name: String, type: AttackAnimationType, builder: AbilityBuilder.() -> Unit): AbilityActionChoice {
        return AbilityActionChoice(name = name, ability = AbilityBuilder(type).apply(builder).build())
    }
}