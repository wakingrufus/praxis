package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.collections.bind
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import com.github.wakingrufus.rpg.inventory.InventoryItem
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.layout.HBox

class FieldInventoryMenuComponent : Component() {
    val list: ObservableList<Pair<InventoryItem, Int>> = FXCollections.observableArrayList()
    override fun onAdded() {
        val window = getUIFactoryService().newWindow()
        window.contentPane.children.add(HBox().apply {
            children.bind(list) {
                getUIFactoryService().newText("${it.first.name}: ${it.second}")
            }
        })
        window.title = "Inventory"
        getGameScene().addUINode(window)
    }

    override fun onUpdate(tpf: Double) {
        list.clear()
        list.addAll(getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<InventoryComponent>().byName())
    }
}
