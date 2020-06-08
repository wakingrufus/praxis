package com.github.wakingrufus.rpg.entities

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.field.MonsterAggroComponent
import com.github.wakingrufus.rpg.PlayerComponent
import com.github.wakingrufus.rpg.field.SpawnerComponent
import com.github.wakingrufus.rpg.battle.*
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.battle.ability.PrayComponent
import com.github.wakingrufus.rpg.battle.ability.WeaponComponent
import com.github.wakingrufus.rpg.field.FieldAnimationComponent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class MyEntityFactory : EntityFactory {
    private val log = Logger.get(javaClass)

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.PLAYER)
                .at(100.0, 100.0)
                .from(data)
                // 1. define hit boxes manually
                .bbox(HitBox(BoundingShape.box(16.0, 16.0)))
                .view(Rectangle(16.0, 16.0, Color.BLUE))
                // 2. make it collidable
                .collidable()
                .with(PlayerComponent())
                .build()
    }

    @Spawns("spawner")
    fun spawner(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.SPAWNER)
                .at(data.x, data.y)
                .with(SpawnerComponent(data))
                .build()
    }

    @Spawns("enemy")
    fun newEnemy(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.ENEMY)
                .at(data.x, data.y)
                .bbox(HitBox(BoundingShape.box(40.0, 40.0)))
                .collidable()
                .with(MonsterAggroComponent(data.get("name"), data.get("party")))
                .with(FieldAnimationComponent(data.get("sprite")))
                .build()
    }
//
//    @Spawns("enemyPartyMember")
//    fun enemyPartyMember(data: SpawnData): Entity {
//        return FXGL.entityBuilder()
//                .type(EntityType.ENEMY_PARTY)
//                .at(data.x, data.y)
//                .zIndex(2)
//                .with(BattleComponent(data.get("name"), data.get("maxHp"), data.get("speed")))
//                .with(BattleAiComponent(Attacker()))
//                .with(EnemyBattleComponent())
//                .with(BattleAnimationComponent(data.get("sprite"), Orientation.LEFT))
//                .build()
//    }

    @Spawns("wall")
    fun newWall(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.OBJECT)
                .at(data.x, data.y)
                // 1. define hit boxes manually
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                // 2. make it collidable
                .build()
    }

    @Spawns("rectangle")
    fun newRectangle(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.OBJECT)
                .at(data.x, data.y)
                // 1. define hit boxes manually
                .viewWithBBox(Rectangle(data.get("width"), data.get("height"), data.get("color")))
                // 2. make it collidable
                .build()
    }

    @Spawns("partyMember")
    fun partyMember(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.PARTY)
                .at(data.x, data.y)
                .zIndex(2)
                .view(Rectangle(40.0, 40.0, Color.BLUE))
                .with(BattleComponent(data.get("name"), data.get("maxHp"), data.get("speed")))
                .with(HealPowerComponent(1))
                .with(AbilitiesComponent())
                .with(WeaponComponent(data.get("weapon")))
                .with(PrayComponent())
                .with(PartyBattleComponent(data.get("partyOrder")))
                .build()
    }
}
