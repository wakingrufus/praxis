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
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.scene.text.Text

@RequiredComponents(
        Required(BattleComponent::class),
        Required(AbilitiesComponent::class))
class PartyBattleComponent(val playerOrder: Int) : Component() {
    private val log = Logger.get(javaClass)
    lateinit var node: Node
    var infoNode: Node? = null
    lateinit var nameNode: Text
    lateinit var currentHp: Text
    lateinit var maxHp: Text

    fun triggerAction(battleActionChoice: BattleActionChoice) {
        log.info("action chosen ${battleActionChoice.name}")
        if (battleActionChoice.ability.requiresTarget) {
            val targetChooserNode = battleDialog {
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
            getGameScene().removeUINode(node)
            getGameScene().addUINode(targetChooserNode)
        } else {
            entity.battleComponent().queueAction(battleActionChoice.toBattleAction(
                    performer = entity.battleComponent(),
                    allies = getGameWorld().getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                    enemies = getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
            )
            getGameState().setValue(BattleStateKeys.activePartyMember, false)
            getGameScene().removeUINode(node)
        }

    }

    override fun onUpdate(tpf: Double) {
        if (infoNode == null) {
            nameNode = FXGL.getUIFactoryService().newText(this.entity.battleComponent().name)
            currentHp = FXGL.getUIFactoryService().newText(this.entity.battleComponent().currentHp.toString())
            maxHp = FXGL.getUIFactoryService().newText(this.entity.battleComponent().maxHp.toString())
            infoNode = HBox().apply {
                background = Background(BackgroundFill(Color.BLACK, null, null))
                translateX = 960.0
                translateY = 800.0 + (playerOrder * 50)
                minWidth = 480.0
                maxWidth = 480.0
                minHeight = 40.0
                maxHeight = 40.0
                children.add(nameNode)
                children.add(currentHp)
                children.add(maxHp)
            }.also {
                getGameScene().addUINode(it)
            }

        }
        currentHp.text = this.entity.battleComponent().currentHp.toString()
        if (entity.battleComponent().canTakeTurn()) {
            if (!getGameState().getBoolean(BattleStateKeys.activePartyMember)) {
                val abilities = entity.getComponent<AbilitiesComponent>().getAbilities()
                getGameState().setValue(BattleStateKeys.activePartyMember, true)
                node = battleDialog {
                    title(entity.battleComponent().name)
                    abilities.forEach { ability ->
                        choice(ability.name) {
                            triggerAction(ability)
                        }
                    }
                }
                getGameScene().addUINode(node)
            }
        }
        if (getGameWorld().getEntitiesByType(EntityType.BATTLE).isEmpty()) {
            infoNode?.also {
                getGameScene().removeUINode(it)
            }

        }
    }

    override fun onRemoved() {
        infoNode?.also {
            getGameScene().removeUINode(it)
        }
        getGameScene().removeUINode(nameNode)
        getGameScene().removeUINode(currentHp)
        getGameScene().removeUINode(maxHp)
        getGameScene().removeUINode(node)
    }
}
