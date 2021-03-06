package com.github.wakingrufus.rpg

import com.almasb.fxgl.app.GameApplication
import com.almasb.fxgl.app.GameSettings
import com.almasb.fxgl.app.MenuItem
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.praxis.PraxisAreaDb
import com.github.wakingrufus.praxis.PraxisItemDb
import com.github.wakingrufus.praxis.PraxisRecipesDb
import com.github.wakingrufus.praxis.PraxisSkillDb
import com.github.wakingrufus.praxis.PraxisSpriteDb.blueLongHawk
import com.github.wakingrufus.praxis.PraxisSpriteDb.maleLight
import com.github.wakingrufus.rpg.area.AreaLoader
import com.github.wakingrufus.rpg.battle.BattleEngine
import com.github.wakingrufus.rpg.battle.BattleStateKeys
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.entities.MyEntityFactory
import com.github.wakingrufus.rpg.field.*
import com.github.wakingrufus.rpg.party.PartyMember
import com.github.wakingrufus.rpg.sprites.characterSpriteSheet
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
        vars["debug"] = true
        vars[BattleStateKeys.inBattle] = false
        vars["mainSpriteSheet"] = characterSpriteSheet(base = maleLight, hat = blueLongHawk)
        vars["pronouns"] = listOf("they", "them", "their", "theirs")
        vars[BattleStateKeys.activePartyMember] = false
        vars[BattleStateKeys.turn] = 0
        vars["party"] = listOf(
                PartyMember(name = "player 1", maxHp = 100, speed = 15,
                        weapon = PraxisRecipesDb.ShortSword.craft(listOf(PraxisItemDb.Iron, PraxisItemDb.Iron, PraxisItemDb.Iron, PraxisItemDb.Oak, PraxisItemDb.Topaz)),
                        skills = listOf(PraxisSkillDb.attack, PraxisSkillDb.pray)),
                PartyMember(name = "player 2", maxHp = 80, speed = 18,
                        skills = listOf(PraxisSkillDb.attack, PraxisSkillDb.nuke, PraxisSkillDb.item)))
    }

    override fun initGame() {
        getGameWorld().addEntityFactory(entityFactory)
        areaLoader = AreaLoader(getGameWorld())
        areaLoader.loadArea(PraxisAreaDb.badlands)
        player = getGameWorld().spawn("player").also {
            playerComponent = it.getComponent()
            getGameScene().viewport.bindToEntity(it, getGameScene().viewport.width / 2, getGameScene().viewport.height / 2)
        }
    }

    override fun initUI() {

    }

    override fun initInput() {
        onKey(KeyCode.D, ::movePlayerEast)
        onKey(KeyCode.A, ::movePlayerWest)
        onKey(KeyCode.W, ::movePlayerNorth)
        onKey(KeyCode.S, ::movePlayerSouth)
        onKeyUp(KeyCode.Q, "Menu") {
            FXGL.getSceneService().pushSubScene(FieldMenu())
        }
        onKeyUp(KeyCode.E, "Activate") {
            playerComponent?.activate()
        }
    }

    override fun initPhysics() {
        FXGL.onCollision(EntityType.PLAYER, EntityType.ENEMY,
                BiConsumer { player: Entity?, enemy: Entity? ->
                    println("On Collision")
                    getGameScene().viewport.zoomProperty().set(1.0)
                    enemy?.also {
                        FXGL.entityBuilder()
                                .type(EntityType.BATTLE)
                                .at(0.0, 0.0)
                                .with(BattleEngine(getGameScene(), it.getComponent()))
                                .buildAndAttach()
                        getGameWorld().removeEntity(it)
                    }
                })
        FXGL.onCollision(EntityType.PLAYER, EntityType.CAMERA,
                BiConsumer { player, camera ->
                    val zoom = camera.getComponent<CameraComponent>().zoomLevel
                    getGameScene().viewport.zoomProperty().set(zoom)
                    getGameScene().viewport.bindToEntity(player,
                            getGameScene().viewport.width / (2 * zoom), getGameScene().viewport.height / (2 * zoom))
                    getGameScene().viewport.focusOn(player)
                }
        )
    }
}

fun main(args: Array<String>) {
    GameApplication.launch(Game::class.java, args)
}

