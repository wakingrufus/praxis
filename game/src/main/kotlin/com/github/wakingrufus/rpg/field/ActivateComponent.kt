package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.praxis.PraxisItemDb.Potion
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import com.github.wakingrufus.rpg.npc.Conversation

class ActivateComponent(val conversation: Conversation?) : Component() {
    private val log = Logger.get(javaClass)
    fun activate(): Unit {
        log.info("activate")
        conversation?.steps?.forEach {
            FXGL.getDialogService().showMessageBox(it.text)
        }
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
                .getComponent<InventoryComponent>().add(Potion, 1)
    }
}
