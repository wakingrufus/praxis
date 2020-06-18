package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.inventory.Armor
import com.github.wakingrufus.rpg.inventory.Weapon

class EquipmentComponent(val helm: Armor? = null, val weapon: Weapon?) : Component() {
    val allEquipment = listOfNotNull(helm, weapon)
    fun attackModifier(damageType: DamageType): Int {
        return allEquipment.sumBy { it.attackDamageModifiers.getOrDefault(damageType, 0) }
    }
    fun defenseModifier(damageType: DamageType): Int {
        return allEquipment.sumBy { it.defenseDamageModifiers.getOrDefault(damageType, 0) }
    }
    fun attackDamageType(): DamageType{
        return weapon?.primaryDamageType ?: DamageType.MELEE
    }
}
