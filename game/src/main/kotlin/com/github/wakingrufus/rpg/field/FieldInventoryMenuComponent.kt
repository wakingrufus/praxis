package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import javafx.scene.layout.Pane

class FieldInventoryMenuComponent : Component() {
    lateinit var pane: Pane
    override fun onAdded() {
        val window = getUIFactoryService().newWindow()
        window.title = "Inventory"
        pane = window.contentPane
        window.canClose = false
        getGameScene().addUINode(window)
    }

    override fun onUpdate(tpf: Double) {
        pane.children.setAll(getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<InventoryComponent>().byName()
                .map { getUIFactoryService().newText("${it.first.name}: ${it.second}") })
    }
}
