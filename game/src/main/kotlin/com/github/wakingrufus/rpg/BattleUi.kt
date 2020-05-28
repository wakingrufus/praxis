package com.github.wakingrufus.rpg

import com.almasb.fxgl.dsl.FXGL.Companion.getUIFactory
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.entity.Entity
import com.github.wakingrufus.collections.bind
import com.github.wakingrufus.rpg.battle.BattleComponent
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

fun enemyStats(enemies: ObservableList<Entity>): Node {
    return VBox().apply {
        isVisible = false
        translateX = 1440.0
        translateY = 800.0
        minWidth = 480.0
        maxWidth = 480.0
        minHeight = 280.0
        maxHeight = 280.0
        background = Background(BackgroundFill(Color.BLACK, null, null))
        children.bind(enemies) {
            HBox().apply {
                children.add(getUIFactory().newText(it.getComponent<BattleComponent>().name))
                children.add(getUIFactory().newText(it.getComponent<BattleComponent>().maxHp.toString()))
            }

        }
        getGameState().booleanProperty("battle").addListener { o, old, newValue ->
            isVisible = newValue
        }
    }
}

fun partyStats(party: ObservableList<Entity>): Node {
    return VBox().apply {
        isVisible = false
        translateX = 960.0
        translateY = 800.0
        minWidth = 480.0
        maxWidth = 480.0
        minHeight = 280.0
        maxHeight = 280.0
        background = Background(BackgroundFill(Color.BLACK, null, null))
        children.bind(party) {
            HBox().apply {
                children.add(getUIFactory().newText(it.getComponent<BattleComponent>().name))
                children.add(getUIFactory().newText(it.getComponent<BattleComponent>().maxHp.toString()))
            }

        }
        getGameState().booleanProperty("battle").addListener { o, old, newValue ->
            isVisible = newValue
        }
    }
}
