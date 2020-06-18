package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.inventory.ItemDb

object PraxisItemDb : ItemDb() {
    val Potion = consumable("Potion") {
        performerEffect { it.heal(5) }
    }

    val Iron = material("Iron", Metal) {
        attack(1, DamageType.MELEE)
    }

    val Bronze = material("Bronze", Metal) {
        attack(2, DamageType.MELEE)
    }

    val Oak = material("Oak", Wood) {
        attack(2, DamageType.RANGE)
        attack(1, DamageType.MELEE)
    }

    val Topaz = material("Topaz", Augment) {
        attack(1, DamageType.FIRE)
    }
}
