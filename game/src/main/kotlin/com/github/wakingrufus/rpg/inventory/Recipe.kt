package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.DamageType

@RpgDsl
open class RecipesDb {
    fun armorRecipe(name: String, slot: EquipmentSlot, builder: ArmorRecipeBuilder.() -> Unit): ArmorRecipe {
        return ArmorRecipeBuilder(name, slot).apply(builder).build()
    }

    fun weaponRecipe(name: String, damageType: DamageType, builder: WeaponRecipeBuilder.() -> Unit): WeaponRecipe {
        return WeaponRecipeBuilder(name, damageType).apply(builder).build()
    }
}

@RpgDsl
sealed class RecipeBuilder(open val name: String, open val slot: EquipmentSlot) {
    protected val ingredients = mutableMapOf<MaterialType, Int>()
    fun add(amt: Int, type: MaterialType) {
        ingredients[type] = ingredients.getOrDefault(type, 0) + amt
    }

    abstract fun build(): Recipe
}

@RpgDsl
class ArmorRecipeBuilder(override val name: String, override val slot: EquipmentSlot) : RecipeBuilder(name, slot) {

    override fun build(): ArmorRecipe {
        return ArmorRecipe(name, slot, ingredients)
    }
}

@RpgDsl
class WeaponRecipeBuilder(override val name: String, val damageType: DamageType) : RecipeBuilder(name, EquipmentSlot.WEAPON) {

    override fun build(): WeaponRecipe {
        return WeaponRecipe(name, damageType, ingredients)
    }
}

sealed class Recipe(open val name: String,
                    open val slot: EquipmentSlot,
                    open val requiredIngredients: Map<MaterialType, Int>) {

    fun canCraft(ingredients: List<CraftingMaterial>): Boolean {
        return requiredIngredients.all { (req, amt) ->
            ingredients.filter { it.type == req }.size == amt
        }
    }

    abstract fun craft(ingredients: List<CraftingMaterial>): Equipment
}

class WeaponRecipe(override val name: String, val damageType: DamageType, override val requiredIngredients: Map<MaterialType, Int>)
    : Recipe(name, EquipmentSlot.WEAPON, requiredIngredients) {

    override fun craft(ingredients: List<CraftingMaterial>): Weapon {
        return WeaponFactory(name, damageType).apply {
            ingredients.forEach { this.apply(it.craftingEffect) }
        }.build()
    }
}

class ArmorRecipe(override val name: String,
                  override val slot: EquipmentSlot,
                  override val requiredIngredients: Map<MaterialType, Int>)
    : Recipe(name, slot, requiredIngredients) {

    override fun craft(ingredients: List<CraftingMaterial>): Armor {
        return ArmorFactory(name, slot).apply {
            ingredients.forEach { this.apply(it.craftingEffect) }
        }.build()
    }
}
