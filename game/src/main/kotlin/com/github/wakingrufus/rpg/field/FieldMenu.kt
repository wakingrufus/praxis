package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.scene.Scene
import com.almasb.fxgl.scene.SubScene
import com.almasb.fxgl.ui.MDIWindow
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import javafx.scene.input.KeyCode

class FieldMenu : SubScene() {
    lateinit var window: MDIWindow
    override fun onCreate() {
        input.addAction(object : UserAction("Exit Menu") {
            override fun onActionEnd() {
                FXGL.getSceneService().popSubScene()
            }
        }, KeyCode.Q)
        window = getUIFactoryService().newWindow().apply {
            title = "Inventory"
            canClose = false
            canMinimize = false
            minWidth = 800.0
        }
        getGameScene().addUINode(window)
    }

    override fun onExitingTo(nextState: Scene) {
        getGameScene().removeUINode(window)
    }

    override fun onUpdate(tpf: Double) {
        window.contentPane.children.setAll(getGameWorld()
                .getEntitiesByType(EntityType.PLAYER).first().getComponent<InventoryComponent>().byName()
                .map { getUIFactoryService().newText("${it.first.name}: ${it.second}") })

    }
}