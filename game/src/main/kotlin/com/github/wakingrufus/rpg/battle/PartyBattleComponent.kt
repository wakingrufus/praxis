package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.component.RequiredComponents
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.ui.ProgressBar
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Text

@RequiredComponents(
        Required(BattleComponent::class),
        Required(AbilitiesComponent::class))
class PartyBattleComponent(val playerOrder: Int) : Component() {
    private val log = Logger.get(javaClass)
    lateinit var node: Node
    var infoNode: Node? = null
    val currentHp = SimpleDoubleProperty(0.0)

    fun triggerAction(battleActionChoice: AbilityActionChoice) {
        log.info("action chosen ${battleActionChoice.name}")
        if (battleActionChoice.ability.requiresTarget) {
            node = battleDialog {
                getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).forEach {
                    choice(it.battleComponent().name) {
                        entity.battleComponent().queueAction(battleActionChoice.toBattleAction(
                                performer = entity.battleComponent(),
                                target = it.battleComponent(),
                                allies = getGameWorld().getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                                enemies = getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
                        )
                        getGameState().setValue(BattleStateKeys.activePartyMember, false)
                    }
                }
            }
            getGameScene().addUINode(node)
        } else {
            entity.battleComponent().queueAction(battleActionChoice.toBattleAction(
                    performer = entity.battleComponent(),
                    allies = getGameWorld().getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                    enemies = getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
            )
            getGameState().setValue(BattleStateKeys.activePartyMember, false)
            //  getGameScene().removeUINode(node)
        }

    }

    override fun onAdded() {
        val maxHp = this.entity.battleComponent().maxHp
        currentHp.set(this.entity.battleComponent().currentHp.toDouble())
        val nameNode = FXGL.getUIFactoryService().newText(this.entity.battleComponent().name)

        val currentHpText = FXGL.getUIFactoryService().newText(currentHp.value.toInt().toString())
        val maxHpText = FXGL.getUIFactoryService().newText(maxHp.toString())
        val hpBar = ProgressBar().apply {
            this.setMaxValue(maxHp.toDouble())
           currentValueProperty().bind(currentHp)
            this.setWidth(800.0)
        }
        val hpBox = VBox().apply {
            children.add(HBox().apply {
                children.add(currentHpText)
                children.add(FXGL.getUIFactoryService().newText(" / "))
                children.add(maxHpText)
            })
            children.add(hpBar)
        }
        infoNode = HBox().apply {
            background = Background(BackgroundFill(Color.BLACK, null, null))
            translateX = 960.0
            translateY = 800.0 + (playerOrder * 80)
            minWidth = 960.0
            maxWidth = 960.0
            minHeight = 80.0
            maxHeight = 80.0
            children.add(nameNode)
            children.add(hpBox)
        }.also {
            getGameScene().addUINode(it)
        }
        currentHp.addListener { observable, oldValue, newValue ->
            currentHpText.text = newValue.toInt().toString()
          //  hpBar.currentValue = newValue.toDouble()
        }
    }

    override fun onUpdate(tpf: Double) {
        currentHp.set(this.entity.battleComponent().currentHp.toDouble())
        if (entity.battleComponent().canTakeTurn()) {
            if (!getGameState().getBoolean(BattleStateKeys.activePartyMember)) {
                val abilities = entity.getComponent<AbilitiesComponent>().getAbilities()
                getGameState().setValue(BattleStateKeys.activePartyMember, true)
                promptForChoice(abilities)
            }
        }
        if (getGameWorld().getEntitiesByType(EntityType.BATTLE).isEmpty()) {
            infoNode?.also {
                getGameScene().removeUINode(it)
            }

        }
    }

    private fun promptForChoice(choices: List<BattleActionChoice>) {
        getGameScene().addUINode(battleDialog {
            title(entity.battleComponent().name)
            choices.forEach { ability ->
                choice(ability.name) {
                    when (ability) {
                        is AbilityActionChoice -> triggerAction(ability)
                        is ChooseAbilityActionChoice -> promptForChoice(ability.choices())
                    }
                }
            }
        }.also { node = it })
    }

    override fun onRemoved() {
        infoNode?.also {
            getGameScene().removeUINode(it)
        }
        getGameScene().removeUINode(node)
    }
}
