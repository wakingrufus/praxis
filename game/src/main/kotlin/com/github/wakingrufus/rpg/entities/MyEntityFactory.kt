package com.github.wakingrufus.rpg.entities

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.EntityFactory
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.Spawns
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.field.CameraComponent
import com.github.wakingrufus.rpg.field.FieldComponent
import com.github.wakingrufus.rpg.field.FieldLpcAnimationComponent
import com.github.wakingrufus.rpg.field.FieldMovementComponent
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class MyEntityFactory : EntityFactory {
    private val log = Logger.get(javaClass)

    @Spawns("player")
    fun newPlayer(data: SpawnData): Entity {
        return FXGL.entityBuilder(data)
                .type(EntityType.PLAYER)
                .at(96.0, 56.0)
                .apply {
                    if (getGameState().getBoolean("debug")) {
                        this.view(Rectangle(32.0, 48.0, Color.BLUE).apply {
                            this.translateX = 16.0
                            this.translateY = 16.0
                            this.opacity = 0.2
                        })
                    }
                }
                .bbox(HitBox(Point2D(16.0, 16.0), BoundingShape.box(32.0, 48.0)))
                .collidable()
                .with(FieldComponent())
                .with(FieldLpcAnimationComponent(getGameState().getObject("mainSpriteSheet")))
                .with(InventoryComponent())
                .with(FieldMovementComponent(2))
                .build()
    }

    @Spawns("wall")
    fun newWall(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.OBJECT)
                .at(data.x, data.y)
                .apply {
                    if (getGameState().getBoolean("debug")) {
                        this.view(Rectangle(data.get("width"), data.get("height"), Color.BLUE))
                    }
                }
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .build()
    }

    @Spawns("zoom")
    fun newCamera(data: SpawnData): Entity {
        return FXGL.entityBuilder()
                .type(EntityType.CAMERA)
                .at(data.x, data.y)
                .apply {
                    if (getGameState().getBoolean("debug")) {
                        this.view(Rectangle(data.get("width"), data.get("height"), Color.GREENYELLOW).apply {
                            this.opacity = 0.1
                        })
                    }
                }
                .bbox(HitBox(BoundingShape.box(data.get("width"), data.get("height"))))
                .collidable()
                .with(CameraComponent(data.get("zoom")))
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
                .collidable()
                .build()
    }
}
