package com.github.wakingrufus.rpg.inventory

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.BattleAnimationComponent
import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.status.StatusEffect

@RpgDsl
sealed class EquipmentFactory(open val baseName: String, open val slot: EquipmentSlot) {
    val attackDamageModifiers: MutableMap<DamageType, Int> = mutableMapOf()
    val defenseDamageModifiers: MutableMap<DamageType, Int> = mutableMapOf()
    val statusImmunities = mutableListOf<StatusEffect>()
    private var animation: BattleAnimationComponent.() -> Unit = {}

    fun attack(power: Int, damageType: DamageType) {
        attackDamageModifiers[damageType] = attackDamageModifiers.getOrDefault(damageType, 0) + power
    }

    abstract fun build(): Equipment
}

@RpgDsl
class ArmorFactory(override val baseName: String, override val slot: EquipmentSlot) : EquipmentFactory(baseName, slot) {
    override fun build(): Armor {
        return Armor(name = baseName,
                slot = slot,
                attackDamageModifiers = attackDamageModifiers,
                defenseDamageModifiers = defenseDamageModifiers,
                statusImmunities = statusImmunities)
    }
}

@RpgDsl
class WeaponFactory(override val baseName: String, val damageType: DamageType) : EquipmentFactory(baseName, EquipmentSlot.WEAPON) {
    override fun build(): Weapon {
        return Weapon(name = baseName,
                attackDamageModifiers = attackDamageModifiers,
                defenseDamageModifiers = defenseDamageModifiers,
                primaryDamageType = damageType,
                statusImmunities = statusImmunities)
    }
}