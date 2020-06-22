package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.battle.ability.AbilityBuilder
import com.github.wakingrufus.rpg.sprites.AttackAnimationType
import com.github.wakingrufus.rpg.status.StatusEffect

sealed class InventoryItem(open val name: String)

class CraftingMaterial(override val name: String, val type: MaterialType,
                       val craftingEffects: Map<EquipmentSlot,EquipmentFactory.() -> Unit>) : InventoryItem(name)

class KeyItem(override val name: String) : InventoryItem(name)
sealed class Equipment(override val name: String,
                       open val slot: EquipmentSlot,
                       open val attackDamageModifiers: Map<DamageType, Int>,
                       open val defenseDamageModifiers: Map<DamageType, Int>,
                       open val statusImmunities: List<StatusEffect>) : InventoryItem(name)

class Armor(override val name: String,
            override val slot: EquipmentSlot,
            override val attackDamageModifiers: Map<DamageType, Int>,
            override val defenseDamageModifiers: Map<DamageType, Int>,
            override val statusImmunities: List<StatusEffect>)
    : Equipment(name, slot, attackDamageModifiers, defenseDamageModifiers, statusImmunities)

class Weapon(override val name: String,
             override val attackDamageModifiers: Map<DamageType, Int>,
             override val defenseDamageModifiers: Map<DamageType, Int>,
             override val statusImmunities: List<StatusEffect>,
             val primaryDamageType: DamageType,
val attackAnimationType: AttackAnimationType)
    : Equipment(name, EquipmentSlot.WEAPON, attackDamageModifiers, defenseDamageModifiers, statusImmunities)

class Consumable(override val name: String, val ability: AbilityBuilder) : InventoryItem(name)
enum class EquipmentSlot {
    WEAPON, ARMOR, BOOTS, ACCESSORY
}

open class MaterialType(val name: String)
