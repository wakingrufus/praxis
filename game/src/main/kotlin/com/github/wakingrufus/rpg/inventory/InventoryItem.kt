package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.battle.ability.Ability

sealed class InventoryItem(open val name: String)

class Equipment(override val name: String, val slot: EquipmentSlot) : InventoryItem(name)
class Consumable(override val name: String, val ability: Ability) : InventoryItem(name)
enum class EquipmentSlot {
    WEAPON, ARMOR, ACCESSORY
}