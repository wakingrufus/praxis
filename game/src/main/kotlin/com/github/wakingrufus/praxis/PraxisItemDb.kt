package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.inventory.ItemDb

object PraxisItemDb : ItemDb() {
    val Potion = consumable("Potion") {
        performerEffect { it.heal(5) }
    }
}
