package com.github.wakingrufus.rpg.enemies

import com.almasb.fxgl.dsl.random
import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.inventory.InventoryItem

class LootTable(val items: List<Pair<Int, InventoryItem>>) {
    fun rollLoot(): List<InventoryItem> {
        return random(1, 100).let { roll ->
            items.filter { roll <= it.first }
        }.map { it.second }
    }
}

@RpgDsl
class LootTableBuilder {
    private val items: MutableList<Pair<Int, InventoryItem>> = mutableListOf()
    fun item(pctChance: Int, item: InventoryItem) {
        items.add(pctChance to item)
    }

    fun build(): LootTable {
        return LootTable(items)
    }
}