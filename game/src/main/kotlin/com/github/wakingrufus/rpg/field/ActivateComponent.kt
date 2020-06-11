package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import com.github.wakingrufus.rpg.inventory.PraxisItemDb.Potion

class ActivateComponent : Component() {
    private val log = Logger.get(javaClass)
    fun activate(): Unit {
        log.info("activate")
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
                .getComponent<InventoryComponent>().add(Potion, 1)
    }
}
