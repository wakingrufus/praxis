package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.app.scene.GameScene
import com.almasb.fxgl.app.scene.GameView
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.Weapon
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.battle.ability.ItemComponent
import com.github.wakingrufus.rpg.battle.ability.PrayComponent
import com.github.wakingrufus.rpg.battle.ability.WeaponComponent
import com.github.wakingrufus.rpg.enemies.BattleParty
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.field.MonsterAggroComponent
import com.github.wakingrufus.rpg.field.MonsterDespawnEvent
import com.github.wakingrufus.rpg.inventory.addItemToInventory
import javafx.scene.image.ImageView

class BattleEngine(val gameScene: GameScene, val enemy: Entity) : Component() {
    private val log = Logger.get(javaClass)
    private lateinit var worldEnemy: Entity
    private lateinit var battleView: GameView
    private lateinit var battleParty: BattleParty

    override fun onAdded() {
        gameScene.gameWorld.properties.setValue("battle", true)
        gameScene.gameWorld.removeEntity(enemy)
        worldEnemy = enemy
        battleView = GameView(ImageView(getAssetLoader().loadImage("battle_valley.png")).apply {
            fitWidth = 1920.0
            fitHeight = 800.0
        }, 1)
        gameScene.addGameView(battleView)
        battleParty = enemy.getComponent<MonsterAggroComponent>().enemies
        battleParty.enemies
                .also { log.info("fighting ${it.size} monsters") }
                .mapIndexed { index, spawnData ->
                    FXGL.entityBuilder()
                            .type(EntityType.ENEMY_PARTY)
                            .at(1500.0, 500.0 + (index * 50))
                            .zIndex(2)
                            .with(BattleComponent(spawnData.name, spawnData.maxHp, spawnData.speed))
                            .with(BattleAiComponent(Attacker()))
                            .with(EnemyBattleComponent())
                            .with(BattleAnimationComponent(spawnData.sprite, Orientation.LEFT))
                            .buildAndAttach()
                }
        val partyEntity = FXGL.entityBuilder()
                .type(EntityType.PARTY)
                .at(100.0, 500.0)
                .zIndex(2)
                .with(BattleAnimationComponent("main", Orientation.RIGHT))
                .with(BattleComponent("Player", 100, 15))
                .with(HealPowerComponent(1))
                .with(AbilitiesComponent())
                .with(WeaponComponent(Weapon("Dagger", 10, DamageType.MELEE)))
                .with(PrayComponent())
                .with(PartyBattleComponent(0))
                .with(ItemComponent())
                .buildAndAttach()
        gameScene.viewport.bindToEntity(partyEntity, partyEntity.x, partyEntity.y)
        getGameState().intProperty(BattleStateKeys.turn).set(1)
    }

    @Override
    override fun onUpdate(tpf: Double) {
        if (getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).isEmpty()) {
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
        val loot = battleParty.lootTable.rollLoot()
        FXGL.getDialogService().showMessageBox("Victory!\nYou Obtained:\n" + loot.joinToString("\n") { it.name })
        loot.forEach { addItemToInventory(it, 1) }
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