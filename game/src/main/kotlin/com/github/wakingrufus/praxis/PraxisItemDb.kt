package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.inventory.EquipmentSlot
import com.github.wakingrufus.rpg.inventory.ItemDb

object PraxisItemDb : ItemDb() {
    val Potion = consumable("Potion") {
        performerEffect { it.heal(5) }
    }

    val Iron = material("Iron", Metal) {
        on(EquipmentSlot.WEAPON){
            attack(2, MELEE)
            attack(2, RANGE)
        }
    }

    val Stone = material("Stone", Metal){
        on(EquipmentSlot.WEAPON){
            attack(1, MELEE)
            attack(1, RANGE)
        }
    }

    val Bronze = material("Bronze", Metal) {
        on(EquipmentSlot.WEAPON) { attack(3, MELEE) }
    }

    val Oak = material("Oak", Wood) {
        on(EquipmentSlot.WEAPON) { attack(2, RANGE) }
        on(EquipmentSlot.WEAPON) { attack(1, MELEE) }
    }

    val Topaz = material("Topaz", Augment) {
        on(EquipmentSlot.ACCESSORY) { attack(1, FIRE) }
        on(EquipmentSlot.WEAPON) { proc(10, PraxisStatusEffectDb.Burn) }
        on(EquipmentSlot.ARMOR) { defense(1, FIRE) }
    }

    val Cotton = material("Cotton", Cloth){
        on(EquipmentSlot.ARMOR) { defense(1, ICE)}
    }
}
