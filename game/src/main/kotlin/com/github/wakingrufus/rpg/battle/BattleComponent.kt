package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getUIFactoryService
import com.almasb.fxgl.dsl.runOnce
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.inventory.Armor
import com.github.wakingrufus.rpg.inventory.Weapon
import com.github.wakingrufus.rpg.sprites.AttackAnimationType
import com.github.wakingrufus.rpg.status.KO
import com.github.wakingrufus.rpg.status.StatusEffect
import javafx.scene.paint.Color
import javafx.util.Duration
import java.lang.Integer.max
import java.lang.Integer.min

class BattleComponent(val name: String, val maxHp: Int, var speed: Int, var currentHp: Int = maxHp,
                      val helm: Armor? = null, val weapon: Weapon? = null,
                      private val startingStatusEffects: List<StatusEffect> = listOf()) : Component() {
    private val log = Logger.get(javaClass)
    private val statusEffects = mutableListOf<StatusEffect>()
    private var active = true
    private val actionQueue = mutableListOf<BattleAction>()

    fun popAction(): BattleAction? {
        return actionQueue.firstOrNull()?.also {
            actionQueue.remove(it)
        }
    }

    override fun onAdded() {
        startingStatusEffects.forEach { applyStatus(it) }
    }

    override fun onUpdate(tpf: Double) {
        statusEffects.forEach {
            it.periodic(this)
        }
    }

    fun applyStatus(statusEffect: StatusEffect) {
        statusEffect.onApply(this)
        statusEffects.add(statusEffect)
    }

    fun removeStatus(statusEffect: StatusEffect) {
        statusEffects.filter { it.name == statusEffect.name }.forEach { it.onRemove(this) }
        statusEffects.removeIf { it.name == statusEffect.name }
    }

    fun activate() {
        active = true
    }

    fun deactivate() {
        active = false
    }

    fun isActive(): Boolean {
        return active
    }

    fun queueAction(action: BattleAction) {
        actionQueue.add(action)
    }

    fun queueIsEmpty(): Boolean = actionQueue.isEmpty()
    fun canTakeTurn(): Boolean = active && getGameState().getInt(BattleStateKeys.turn) % (1000 / speed) == 0
    fun takeDamage(value: Int, type: DamageType) {
        val modifier = defenseModifier(type)
        val adjustedAmt = value - modifier
        log.info("${entity.battleComponent().name} takes $adjustedAmt $type damage")
        currentHp = max(0, currentHp - adjustedAmt)
        displayDamage(adjustedAmt, type.color)
        if (currentHp <= 0) {
            entity.getComponent<BattleAnimationComponent>().die()
            applyStatus(KO)
            actionQueue.clear()
        }
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

    val allEquipment = listOfNotNull(helm, weapon)
    fun attackModifier(damageType: DamageType): Int {
        return allEquipment.sumBy { it.attackDamageModifiers.getOrDefault(damageType, 0) }
    }

    fun defenseModifier(damageType: DamageType): Int {
        return allEquipment.sumBy { it.defenseDamageModifiers.getOrDefault(damageType, 0) }
    }

    fun attackDamageType(): DamageType {
        return weapon?.primaryDamageType ?: DamageType(Color.WHITE)
    }

    fun attackAnimationType(): AttackAnimationType {
        return weapon?.attackAnimationType ?: AttackAnimationType.SLASH
    }
}

fun Entity.battleComponent() = getComponent<BattleComponent>()
