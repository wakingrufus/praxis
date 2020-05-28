package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import javafx.collections.FXCollections
import com.github.wakingrufus.rpg.getComponent


class BattleComponent(val name: String, val maxHp: Int, var speed: Int, var currentHp : Int = maxHp) : Component() {
    private val actionQueue = FXCollections.observableArrayList<BattleAction>()
    fun popAction(): BattleAction? {
        return actionQueue.firstOrNull()?.also {
            actionQueue.remove(it)
        }
    }
    fun queueAction(action: BattleAction){
        actionQueue.add(action)
    }
    fun queueIsEmpty(): Boolean = actionQueue.isEmpty()
    fun canTakeTurn() : Boolean = (1000 / speed) % getGameState().getInt(BattleStateKeys.turn) == 0
    fun takeDamage(value: Int, type: DamageType){
        currentHp -= value
    }
    fun heal(value: Int){
        currentHp += value
    }
}

fun Entity.battleComponent() = getComponent<BattleComponent>()