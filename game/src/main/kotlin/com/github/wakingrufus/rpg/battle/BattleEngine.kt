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
import com.github.wakingrufus.rpg.field.MonsterAggroComponent
import com.github.wakingrufus.rpg.field.MonsterDespawnEvent
import javafx.scene.image.ImageView

class BattleEngine(val gameScene: GameScene, val enemy: Entity) : Component() {
    private val log = Logger.get(javaClass)
    private lateinit var worldEnemy: Entity
    private lateinit var battleView: GameView

    override fun onAdded() {
        gameScene.gameWorld.properties.setValue("battle", true)
        gameScene.gameWorld.removeEntity(enemy)
        worldEnemy = enemy
        battleView = GameView(ImageView(getAssetLoader().loadImage("battle_valley.png")).apply {
            fitWidth = 1920.0
            fitHeight = 800.0
        }, 1)
        gameScene.addGameView(battleView)
        enemy.getComponent<MonsterAggroComponent>().enemies
                .also { log.info("fighting ${it.enemies.size} monsters") }
                .let { it.spawnData() }
                .mapIndexed { index, spawnData ->
                    FXGL.entityBuilder()
                            .type(EntityType.ENEMY_PARTY)
                            .at(1500.0, 500.0 + (index * 50))
                            .zIndex(2)
                            .with(BattleComponent(spawnData.get("name"), spawnData.get("maxHp"), spawnData.get("speed")))
                            .with(BattleAiComponent(Attacker()))
                            .with(EnemyBattleComponent())
                            .with(BattleAnimationComponent(spawnData.get("sprite"), Orientation.LEFT))
                            .buildAndAttach()
                }
       val partyEntity = gameScene.gameWorld.spawn("partyMember", SpawnData(100.0, 500.0).apply {
            put("width", 40.0)
            put("height", 40.0)
            put("name", "player")
            put("maxHp", 100)
            put("speed", 15)
            put("weapon", Weapon("Dagger", 10, DamageType.MELEE))
            put("partyOrder", 0)
        })
        gameScene.viewport.bindToEntity(partyEntity, partyEntity.x, partyEntity.y)
        getGameState().intProperty(BattleStateKeys.turn).set(1)
    }

    @Override
    override fun onUpdate(tpf: Double) {
        if(getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).isEmpty()){
            getGameWorld().removeEntity(this.entity)
        }
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

    override fun onRemoved() {
        log.info("Victory!")
        getEventBus().fireEvent(MonsterDespawnEvent(MonsterDespawnEvent.ANY, worldEnemy))
        getGameWorld().getEntitiesByType(EntityType.PARTY)
                .forEach { getGameWorld().removeEntities(it) }
        gameScene.removeGameView(battleView)
        getGameState().setValue(BattleStateKeys.activePartyMember, false)
        getGameScene().viewport.bindToEntity(getGameWorld().getEntitiesByType(EntityType.PLAYER).first(), 1000.0, 500.0)
    }
}

object BattleStateKeys {
    val turn = "battle.turn"
    val activePartyMember = "battle.activePartyMember"
}