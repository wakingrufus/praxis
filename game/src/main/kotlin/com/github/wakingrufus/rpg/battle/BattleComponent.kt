package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.runOnce
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.battle.ability.EquipmentComponent
import com.github.wakingrufus.rpg.status.StatusEffect
import javafx.scene.paint.Color
import javafx.util.Duration
import java.lang.Integer.max
import java.lang.Integer.min

class BattleComponent(val name: String, val maxHp: Int, var speed: Int, var currentHp: Int = maxHp) : Component() {
    private val log = Logger.get(javaClass)
    private val statusEffects = mutableListOf<StatusEffect>()
    private val actionQueue = mutableListOf<BattleAction>()
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
        val modifier = entity.getComponentOptional(EquipmentComponent::class.java).map {
            it.defenseModifier(type)
        }.orElse(0)
        val adjustedAmt = value - modifier
        log.info("${entity.battleComponent().name} takes $adjustedAmt $type damage")
        currentHp = max(0, currentHp - adjustedAmt)
        displayDamage(adjustedAmt)
    }

    fun heal(value: Int) {
        currentHp = min(maxHp, currentHp + value)
        displayDamage(value, Color.GREEN)
    }

    private fun displayDamage(value: Int, color: Color = Color.WHITE) {
        val dmgDisplay = getUIFactoryService().newText(value.toString()).apply {
            translateX = entity.x
            translateY = entity.y
            this.fill = color
        }
        getGameScene().addUINode(dmgDisplay)
        runOnce({ getGameScene().removeUINode(dmgDisplay) }, Duration.seconds(1.0))
    }
}

fun Entity.battleComponent() = getComponent<BattleComponent>()
