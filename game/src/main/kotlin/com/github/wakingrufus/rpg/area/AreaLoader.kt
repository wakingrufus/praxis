package com.github.wakingrufus.rpg.area

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getbp
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.field.*
import javafx.geometry.Point2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class AreaLoader(val gameWorld: GameWorld) {

    fun loadArea(area: Area) {
        FXGL.setLevelFromMap(area.map)
        area.objects.forEach {
            gameWorld.spawn("rectangle", it)
        }
        area.spawners.forEach {
            FXGL.entityBuilder()
                    .type(EntityType.SPAWNER)
                    .at(it.x, it.y)
                    .with(SpawnerComponent(it))
                    .with(FieldComponent())
                    .buildAndAttach()
        }
        area.npcs.forEach {
            FXGL.entityBuilder()
                    .type(EntityType.NPC)
                    .at(it.x, it.y)
                    .apply {
                        if (getbp("debug").value) {
                            this.view(Rectangle(32.0, 48.0, Color.GREEN).apply {
                                this.translateX = 16.0
                                this.translateY = 16.0
                                this.opacity = 0.2
                            })
                        }
                    }
                    .bbox(HitBox(Point2D(16.0, 16.0), BoundingShape.box(32.0, 48.0)))
                    .with(FieldMovementComponent(1))
                    .with(FieldLpcAnimationComponent(it.sprite))
                    .with(ActivateComponent(conversation = it.conversation))
                    .collidable()
                    .buildAndAttach()
        }
    }
}
