package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.app.scene.GameScene
import com.almasb.fxgl.app.scene.GameView
import com.almasb.fxgl.dsl.*
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.battle.BattleStateKeys.inBattle
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.battle.ability.ItemComponent
import com.github.wakingrufus.rpg.enemies.EnemyParty
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.field.MonsterAggroComponent
import com.github.wakingrufus.rpg.inventory.addItemToInventory
import com.github.wakingrufus.rpg.party.PartyMember
import com.github.wakingrufus.rpg.sprites.SpriteOrientation
import javafx.scene.image.ImageView

class BattleEngine(val gameScene: GameScene, val enemy: Entity) : Component() {
    private val log = Logger.get(javaClass)
    private lateinit var worldEnemy: Entity
    private lateinit var battleView: GameView
    private lateinit var battleParty: EnemyParty

    override fun onAdded() {
        gameScene.gameWorld.properties.setValue(inBattle, true)
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
                .mapIndexed { index, enemy ->
                    FXGL.entityBuilder()
                            .type(EntityType.ENEMY_PARTY)
                            .at(1500.0, 500.0 + (index * 50))
                            .zIndex(2)
                            .with(BattleComponent(enemy.name, enemy.maxHp, enemy.speed))
                            .with(BattleAiComponent(enemy.ai))
                            .with(BattleAnimationComponent(enemy.sprite, SpriteOrientation.WEST))
                            .buildAndAttach()
                }
        val partyEntities = getGameState().getObject<List<PartyMember>>("party").mapIndexed { index, member ->
            FXGL.entityBuilder()
                    .type(EntityType.PARTY)
                    .at(100.0, 500.0 + (index * 50))
                    .zIndex(2)
                    .with(BattleAnimationComponent(getGameState().getObject("mainSpriteSheet"), SpriteOrientation.EAST))
                    .with(BattleComponent(member.name, member.maxHp, member.speed, weapon = member.weapon, helm = member.armor,
                            startingStatusEffects = member.skills.flatMap { it.statusEffects }))
                    .with(AbilitiesComponent(member.skills.flatMap { it.actions }))
                    .with(PartyBattleComponent(index))
                    .with(ItemComponent())
                    .buildAndAttach()
        }
        with(partyEntities.first()) {
            gameScene.viewport.bindToEntity(this, x, y)
        }

        getGameState().intProperty(BattleStateKeys.turn).set(1)
    }

    @Override
    override fun onUpdate(tpf: Double) {
        if (getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY).none { it.battleComponent().isActive() }) {
            getGameWorld().removeEntity(this.entity)
        }
        val allBattleEntities = getGameWorld().getEntitiesByType(EntityType.PARTY)
                .plus(getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY))
        val currentTurnBattleComponents = allBattleEntities
                .map { it.battleComponent() }
                .filter(BattleComponent::isActive)
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
        getGameWorld().getEntitiesByType(EntityType.ENEMY_PARTY)
                .forEach { getGameWorld().removeEntity(it) }
        getGameWorld().getEntitiesByType(EntityType.PARTY)
                .forEach { getGameWorld().removeEntities(it) }
        gameScene.removeGameView(battleView)
        getGameState().setValue(BattleStateKeys.activePartyMember, false)
        getGameScene().viewport.bindToEntity(getGameWorld().getEntitiesByType(EntityType.PLAYER).first(), 1000.0, 500.0)
        gameScene.gameWorld.properties.setValue(inBattle, false)
    }
}

object BattleStateKeys {
    const val inBattle = "battle"
    const val turn = "battle.turn"
    const val activePartyMember = "battle.activePartyMember"
}
