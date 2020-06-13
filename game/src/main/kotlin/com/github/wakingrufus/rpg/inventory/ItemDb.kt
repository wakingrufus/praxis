package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder

@RpgDsl
open class ItemDb {
    fun consumable(name: String, ability: AbilityBuilder.() -> Unit): Consumable {
        //TODO figure out how a consumable can remove itself from the inventory
        return Consumable(name, AbilityBuilder().apply(ability).build())
    }
}
