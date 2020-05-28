package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.app.scene.GameScene
import com.almasb.fxgl.app.scene.GameView
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.*
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.field.MonsterDespawnEvent
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.Node
import javafx.scene.image.ImageView

class BattleEngine(val gameScene: GameScene) : Component() {
    private val log = Logger.get(javaClass)
    private val enemies: ObservableList<Entity> = FXCollections.observableArrayList()
    private val party: ObservableList<Entity> = FXCollections.observableArrayList()
    private val enemyStatsUi: Node = enemyStats(enemies)
    private val partyStatsUi: Node = partyStats(party)
    private lateinit var worldEnemy: Entity

    fun startBattle(enemy: Entity) {
        getGameScene().addUINode(enemyStatsUi)
        getGameScene().addUINode(partyStatsUi)
        gameScene.gameWorld.properties.setValue("battle", true)
        gameScene.gameWorld.removeEntity(enemy)
        worldEnemy = enemy
        gameScene.addGameView(GameView(ImageView(getAssetLoader().loadImage("battle_valley.png")).apply {
            fitWidth = 1920.0
            fitHeight = 800.0
        }, 1))
        enemies.addAll(enemy.getComponent<MonsterAggroComponent>().enemies
                .also { log.info("fighting ${it.size} monsters") }
                .mapIndexed { index, spawnData ->
                    gameScene.gameWorld.spawn("enemyPartyMember",
                            SpawnData(1500.0, 500.0 + (index * 50)).apply {
                                spawnData.data.forEach { this.put(it.key, it.value) }
                            })
                })
        party.addAll(gameScene.gameWorld.spawn("partyMember", SpawnData(100.0, 500.0).apply {
            put("width", 40.0)
            put("height", 40.0)
            put("name", "player")
            put("maxHp", 100)
            put("speed", 15)
            put("weapon", Weapon("Dagger", 10, DamageType.MELEE))
        }))
        gameScene.viewport.bindToEntity(party.first(), party.first().x, party.first().y)
        getGameState().intProperty(BattleStateKeys.turn).set(1)
    }

    @Override
    override fun onUpdate(tpf: Double) {
        val allBattleEntities = getGameWorld().getEntitiesByType(EntityType.PARTY)
                .plus(getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY))
        val currentTurnBattleComponents = allBattleEntities
                .map { it.battleComponent() }
                .filter(BattleComponent::canTakeTurn)
        if (currentTurnBattleComponents.all { !it.queueIsEmpty() }) {
            currentTurnBattleComponents
                    .mapNotNull(BattleComponent::popAction)
                    .forEach {
                        log.info("${it.performer.name} performs action: ${it.name}")
                        it.effect()
                    }
            getGameState().increment(BattleStateKeys.turn, 1)
        }
    }

    fun endBattle() {
        gameScene.removeUINode(enemyStatsUi)
        gameScene.removeUINode(partyStatsUi)
        getEventBus().fireEvent(MonsterDespawnEvent(MonsterDespawnEvent.ANY, worldEnemy))
    }
}

object BattleStateKeys {
    val turn = "battle.turn"
    val activePartyMember = "battle.activePartyMember"
}