package com.github.wakingrufus.rpg.entities

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.battle.BattleComponent
import com.github.wakingrufus.rpg.battle.HealPowerComponent
import com.github.wakingrufus.rpg.battle.PartyBattleComponent
import com.github.wakingrufus.rpg.battle.ability.AbilitiesComponent
import com.github.wakingrufus.rpg.battle.ability.ItemComponent
import com.github.wakingrufus.rpg.battle.ability.PrayComponent
import com.github.wakingrufus.rpg.battle.ability.WeaponComponent
import com.github.wakingrufus.rpg.field.*
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class MyEntityFactory : EntityFactory {
    private val log = Logger.get(javaClass)

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .at(96.0, 56.0)
                .bbox(HitBox(BoundingShape.box(32.0, 56.0)))
                .collidable()
                .with(FieldLpcAnimationComponent("main"))
                .with(InventoryComponent())
                .with(FieldMovementComponent(2))
                .build()
    }

    @Spawns("wall")
    fun newWall(data: SpawnData): Entity {
        log.info(data.data.toString())
        log.info("creating wall at ${data.x},${data.y} with dimensions ${data.get<Int>("width")},${data.get<Int>("height")}")
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
}
