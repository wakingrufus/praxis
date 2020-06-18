package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.inventory.MaterialType
import com.github.wakingrufus.rpg.inventory.RecipesDb

object Metal : MaterialType("Metal")
object Cloth : MaterialType("Cloth")
object Leather : MaterialType("Leather")
object Augment : MaterialType("Augment")
object Wood : MaterialType("Wood")

object PraxisRecipesDb : RecipesDb() {
    val ShortSword = weaponRecipe("Short Sword", DamageType.MELEE) {
        add(3, Metal)
        add(1, Wood)
        add(1, Augment)
    }
}
