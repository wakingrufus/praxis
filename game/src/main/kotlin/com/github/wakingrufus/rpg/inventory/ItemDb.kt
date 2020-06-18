package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder

@RpgDsl
open class ItemDb {

    fun consumable(name: String, ability: AbilityBuilder.() -> Unit): Consumable {
        return Consumable(name, AbilityBuilder().apply(ability))
    }

    fun material(name: String, type: MaterialType, builder: EquipmentFactory.() -> Unit): CraftingMaterial {
        return CraftingMaterial(name, type, builder)
    }
}
