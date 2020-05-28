package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.dsl.FXGL.Companion.getDialogFactoryService
import com.almasb.fxgl.dsl.FXGL.Companion.getDialogService
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.component.RequiredComponents
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.getComponent
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.paint.Paint

@RequiredComponents(
        Required(BattleComponent::class),
        Required(AbilitiesComponent::class))
class PartyBattleComponent : Component() {
    private val log = Logger.get(javaClass)
    lateinit var handler: EventHandler<ActionChoiceEvent>
    lateinit var node: Node

    fun triggerAction(battleActionChoice: BattleActionChoice) {
        log.info("action chosen ${battleActionChoice.name}")
        if (battleActionChoice.ability.requiresTarget) {
            entity.battleComponent().queueAction(battleActionChoice.toBattleAction(
                    performer = entity.battleComponent(),
                    target = getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).random().battleComponent(),
                    allies = getGameWorld().getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                    enemies = getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
            )
        } else {
            entity.battleComponent().queueAction(battleActionChoice.toBattleAction(
                    performer = entity.battleComponent(),
                    allies = getGameWorld().getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                    enemies = getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
            )
        }
        getGameState().setValue(BattleStateKeys.activePartyMember, false)
        getGameScene().removeUINode(node)
        getEventBus().removeEventHandler(ActionChoiceEvent.ANY, this.handler)
    }

    @Override
    override fun onUpdate(tpf: Double) {
        if (entity.battleComponent().canTakeTurn()) {
            if (!getGameState().getBoolean(BattleStateKeys.activePartyMember)) {
                val abilities = entity.getComponent<AbilitiesComponent>().getAbilities()
                getGameState().setValue(BattleStateKeys.activePartyMember, true)
                node = VBox().apply {
                    children.add(getUIFactoryService().newText(entity.battleComponent().name))
                    children.addAll(*abilities
                            .map { ability ->
                                getUIFactoryService().newButton(ability.name).apply {
                                    setOnAction {
                                        triggerAction(ability)
                                    }
                                }
                            }.toTypedArray()
                    ).apply {
                        translateX = 0.0
                        translateY = 800.0
                        minWidth = 480.0
                        maxWidth = 480.0
                        minHeight = 280.0
                        maxHeight = 280.0
                        background = Background(BackgroundFill(Color.BLACK, null, null))
                    }
                }

                getGameScene().addUINode(node)
                handler = EventHandler<ActionChoiceEvent> { event: ActionChoiceEvent ->
                    if (event.choice <= abilities.size) {
                        val selectedAction = abilities[event.choice - 1]
                        triggerAction(selectedAction)
                        getEventBus().removeEventHandler(ActionChoiceEvent.ANY, this.handler)
                    } else {
                        log.warning("Invalid choice")
                    }
                }
            }
        }
    }
}
