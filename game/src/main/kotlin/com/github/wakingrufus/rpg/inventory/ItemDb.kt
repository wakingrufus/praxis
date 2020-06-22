package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

@RpgDsl
open class ItemDb {

    fun consumable(name: String, ability: AbilityBuilder.() -> Unit): Consumable {
        return Consumable(name, AbilityBuilder(AttackAnimationType.SPELL).apply(ability))
    }

    fun material(name: String, type: MaterialType, builder: CraftingMaterialBuilder.() -> Unit): CraftingMaterial {
        return CraftingMaterialBuilder(name, type).apply(builder).build()
    }
}
