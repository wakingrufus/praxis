package com.github.wakingrufus.rpg

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.app.MenuItem
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.input.Input
import com.almasb.fxgl.input.UserAction
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.praxis.PRAXIS_AREAS
import com.github.wakingrufus.rpg.area.AreaLoader
import com.github.wakingrufus.rpg.battle.BattleEngine
import com.github.wakingrufus.rpg.battle.BattleStateKeys
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.entities.MyEntityFactory
import com.github.wakingrufus.rpg.field.*
import javafx.scene.input.KeyCode
import java.util.*
import java.util.function.BiConsumer
import kotlin.collections.set

class Game : GameApplication() {
    private val log = Logger.get(javaClass)
    private var player: Entity? = null
    private var playerComponent: FieldMovementComponent? = null
    private val entityFactory = MyEntityFactory()
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
        vars[BattleStateKeys.activePartyMember] = false
        vars[BattleStateKeys.turn] = 0
    }

    override fun initGame() {
        getGameWorld().addEntityFactory(entityFactory)
        areaLoader = AreaLoader(getGameWorld())
        areaLoader.loadArea(PRAXIS_AREAS.badlands)
        player = getGameWorld().spawn("player").also {
            playerComponent = it.getComponent()
            getGameScene().viewport.bindToEntity(it, 1000.0, 500.0)
        }

    }

    override fun initUI() {
        FXGL.entityBuilder().type(EntityType.DIALOG).with(FieldInventoryMenuComponent()).buildAndAttach()
    }

    override fun initInput() {
        val input: Input = FXGL.getInput()
        onKey(KeyCode.D, ::movePlayerEast)
        onKey(KeyCode.A, ::movePlayerWest)
        onKey(KeyCode.W, ::movePlayerNorth)
        onKey(KeyCode.S, ::movePlayerSouth)
        input.addAction(object : UserAction("Activate") {
            override fun onActionEnd() {
                playerComponent?.activate()
            }
        }, KeyCode.E)
        //     input.addAction(MenuAction(getGameState(), { gameMenu() }, { exitMenu() }), KeyCode.Q)
    }

    override fun initPhysics() {
        FXGL.onCollision(EntityType.PLAYER, EntityType.ENEMY,
                BiConsumer { player: Entity?, enemy: Entity? ->
                    println("On Collision")
                    enemy?.also {
                        FXGL.entityBuilder()
                                .type(EntityType.BATTLE)
                                .at(0.0, 0.0)
                                .with(BattleEngine(getGameScene(), it))
                                .buildAndAttach()
                    }
                })
    }

}

fun main(args: Array<String>) {
    GameApplication.launch(Game::class.java, args)
}

