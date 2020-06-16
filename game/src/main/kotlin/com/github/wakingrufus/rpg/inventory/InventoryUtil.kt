package com.github.wakingrufus.rpg.inventory

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.entities.EntityType

fun addItemToInventory(item: InventoryItem, quantity: Int) {
    getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<InventoryComponent>().add(item, quantity)
}

fun removeItemFromInventory(item: InventoryItem) {
    getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<InventoryComponent>().remove(item)
}
