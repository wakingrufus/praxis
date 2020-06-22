package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.inventory.EquipmentSlot
import com.github.wakingrufus.rpg.inventory.MaterialType
import com.github.wakingrufus.rpg.inventory.RecipesDb
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

object Metal : MaterialType("Metal")
object Cloth : MaterialType("Cloth")
object Leather : MaterialType("Leather")
object Augment : MaterialType("Augment")
object Wood : MaterialType("Wood")

object PraxisRecipesDb : RecipesDb() {
    val ShortSword = weaponRecipe("Short Sword", DamageType.MELEE, AttackAnimationType.SLASH) {
        add(3, Metal)
        add(1, Wood)
        add(1, Augment)
    }

    val Robe = armorRecipe("Robe", EquipmentSlot.ARMOR){
        add(5, Cloth)
        add(2, Augment)
    }

    val LeatherVest = armorRecipe("Leather Vest", EquipmentSlot.ARMOR){
        add(4, Leather)
        add(1, Augment)
    }

    val ChainMail = armorRecipe("Chain Mail", EquipmentSlot.ARMOR){
        add(5, Metal)
        add(1, Augment)
    }

    val PlateArmor = armorRecipe(" Plate Armor", EquipmentSlot.ARMOR){
        add(6, Metal)
    }

    val Moccasins = armorRecipe("Moccasins", EquipmentSlot.BOOTS){
        add(2, Leather)
        add(1, Augment)
    }
}
