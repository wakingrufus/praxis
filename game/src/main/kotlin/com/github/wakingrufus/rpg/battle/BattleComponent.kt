package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.runOnce
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import javafx.collections.FXCollections
import javafx.util.Duration
import java.lang.Integer.min

class BattleComponent(val name: String, val maxHp: Int, var speed: Int, var currentHp: Int = maxHp) : Component() {
    private val actionQueue = FXCollections.observableArrayList<BattleAction>()
    fun popAction(): BattleAction? {
        return actionQueue.firstOrNull()?.also {
            actionQueue.remove(it)
        }
    }

    fun queueAction(action: BattleAction) {
        actionQueue.add(action)
    }

    fun queueIsEmpty(): Boolean = actionQueue.isEmpty()
    fun canTakeTurn(): Boolean = getGameState().getInt(BattleStateKeys.turn) % (1000 / speed) == 0
    fun takeDamage(value: Int, type: DamageType) {
        currentHp -= value
        val dmgDisplay = getUIFactoryService().newText(value.toString()).apply {
            translateX = entity.x
            translateY = entity.y
        }
        getGameScene().addUINode(dmgDisplay)
        runOnce({ getGameScene().removeUINode(dmgDisplay) }, Duration.seconds(1.0))
    }

    fun heal(value: Int) {
        currentHp = min(maxHp, currentHp + value)
    }
}

fun Entity.battleComponent() = getComponent<BattleComponent>()