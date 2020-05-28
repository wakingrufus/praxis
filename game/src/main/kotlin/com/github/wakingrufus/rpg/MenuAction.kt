package com.github.wakingrufus.rpg

import com.almasb.fxgl.core.collection.PropertyMap
import com.almasb.fxgl.input.UserAction

class MenuAction(val gameState: PropertyMap,
                 val openMenu: () -> Unit,
                 val exitMenu: () -> Unit) : UserAction("Menu") {
    override fun onActionEnd() {
        if (gameState.getValueOptional<Boolean>("menu").orElse(false)) {
            exitMenu()
        } else {
            openMenu()
            gameState.setValue("menu", true)
        }
    }
}