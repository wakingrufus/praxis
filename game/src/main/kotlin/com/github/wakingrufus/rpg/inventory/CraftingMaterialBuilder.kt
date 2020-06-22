package com.github.wakingrufus.rpg.inventory

class CraftingMaterialBuilder(val name: String, val type: MaterialType) {
    private val effects: MutableMap<EquipmentSlot, EquipmentFactory.() -> Unit> = mutableMapOf()
    fun on(slot: EquipmentSlot, effect: EquipmentFactory.() -> Unit) {
        effects[slot] = effect
    }

    fun build(): CraftingMaterial {
        return CraftingMaterial(name, type, effects)
    }
}