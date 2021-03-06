package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.ui.MDIWindow
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import javafx.scene.layout.Pane

class FieldInventoryMenuComponent : Component() {
    lateinit var window : MDIWindow
    override fun onAdded() {
        window = getUIFactoryService().newWindow().apply {
            title = "Inventory"
            canClose = false
            canMinimize = false
            minWidth = 800.0
        }
        getGameScene().addUINode(window)
    }

    override fun onUpdate(tpf: Double) {
        window.contentPane.children.setAll(getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<InventoryComponent>().byName()
                .map { getUIFactoryService().newText("${it.first.name}: ${it.second}") })
    }

    override fun onRemoved() {
        getGameScene().removeUINode(window)
    }
}
