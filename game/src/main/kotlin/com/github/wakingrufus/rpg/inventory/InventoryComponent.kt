package com.github.wakingrufus.rpg.inventory

import com.almasb.fxgl.entity.component.Component
import javafx.collections.FXCollections
import javafx.collections.ObservableMap

class InventoryComponent : Component() {
    val items: ObservableMap<InventoryItem, Int> = FXCollections.observableHashMap()
    fun add(item: InventoryItem, quantity: Int) {
        items[item] = items.getOrDefault(item, 0) + quantity
    }

    fun byName(): List<Pair<InventoryItem, Int>> {
        return items.toList().sortedBy { it.first.name }
    }
}
