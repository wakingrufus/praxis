package com.github.wakingrufus.rpg

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.app.MenuItem
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.area.AreaLoader
import com.github.wakingrufus.rpg.battle.ActionChoiceEvent
import com.github.wakingrufus.rpg.battle.BattleEngine
import com.github.wakingrufus.rpg.battle.BattleStateKeys
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.entities.MyEntityFactory
import javafx.event.Event
import javafx.scene.input.KeyCode
import java.util.*
import java.util.function.BiConsumer
import kotlin.collections.set

class Game : GameApplication() {
    private val log = Logger.get(javaClass)
    private var player: Entity? = null
    private var playerComponent: PlayerComponent? = null
    private val entityFactory = MyEntityFactory()
    private lateinit var battleEngine: BattleEngine
    private lateinit var areaLoader: AreaLoader
    override fun initSettings(settings: GameSettings) {
        settings.width = 1920
        settings.height = 1080
        settings.title = "Basic Game App"
        settings.isManualResizeEnabled = true
        settings.isMainMenuEnabled = true
        settings.isGameMenuEnabled = true
        settings.enabledMenuItems = EnumSet.allOf(MenuItem::class.java)
    }

    override fun onPreInit() {
        getSaveLoadService().addHandler(RpgSaveLoadHandler())
    }

    override fun initGameVars(vars: MutableMap<String?, Any?>) {
        vars["pixelsMoved"] = 0
        vars["battle"] = false
        vars[BattleStateKeys.activePartyMember] = false
        vars[BattleStateKeys.turn] = 0
    }

    override fun initGame() {
        getGameWorld().addEntityFactory(entityFactory)
        battleEngine = BattleEngine(getGameScene())
        areaLoader = AreaLoader(getGameWorld())
        areaLoader.loadArea("home")
        player = getGameWorld().spawn("player").also {
            playerComponent = it.getComponent()
            getGameScene().viewport.bindToEntity(it, 1000.0, 500.0)
            getGameState().booleanProperty("battle").addListener { o, old, newValue ->
                it.isVisible = !newValue
            }
        }
    }

    override fun initUI() {
//        getGameScene().addUINode(enemyStats(battleEngine))
//        getGameScene().addUINode(partyStats(battleEngine))
//        getGameScene().addUINode(actionQueue(battleEngine))
    }

    override fun initInput() {
        val input: Input = FXGL.getInput()
        input.addAction(object : UserAction("Move Right") {
            override fun onAction() {
                playerComponent?.moveRight()
                FXGL.getWorldProperties().increment("pixelsMoved", +5)
            }
        }, KeyCode.D)
        input.addAction(object : UserAction("Move Left") {
            override fun onAction() {
                playerComponent?.moveLeft()
                FXGL.getWorldProperties().increment("pixelsMoved", +5)
            }
        }, KeyCode.A)
        input.addAction(object : UserAction("Move Up") {
            override fun onAction() {
                playerComponent?.moveUp()
                FXGL.getWorldProperties().increment("pixelsMoved", +5)
            }
        }, KeyCode.W)
        input.addAction(object : UserAction("Move Down") {
            override fun onAction() {
                playerComponent?.moveDown()
                FXGL.getWorldProperties().increment("pixelsMoved", +5)
            }
        }, KeyCode.S)
        input.addAction(object : UserAction("Choose 1") {
            override fun onActionEnd() {
                getEventBus().fireEvent(ActionChoiceEvent(ActionChoiceEvent.ANY,1))
            }
        }, KeyCode.DIGIT1)
        input.addAction(object : UserAction("Choose 2") {
            override fun onActionEnd() {
                getEventBus().fireEvent(ActionChoiceEvent(ActionChoiceEvent.ANY,2))
            }
        }, KeyCode.DIGIT2)
        //     input.addAction(MenuAction(getGameState(), { gameMenu() }, { exitMenu() }), KeyCode.Q)
    }

    override fun initPhysics() {
        FXGL.onCollision(EntityType.PLAYER, EntityType.ENEMY,
                BiConsumer { player: Entity?, enemy: Entity? ->
                    println("On Collision")
                    enemy?.also { FXGL.entityBuilder()
                            .type(EntityType.BATTLE)
                            .at(0.0,0.0)
                            .with(battleEngine)
                            .buildAndAttach()
                    battleEngine.startBattle(it) }
                })
    }

}

fun main(args: Array<String>) {
    GameApplication.launch(Game::class.java, args)
}

