package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.TransformComponent
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import kotlin.math.withSign

class FieldMovementComponent(val speed: Int) : Component() {
    private val log = Logger.get(javaClass)

    private lateinit var position: TransformComponent
    private var orientation: Orientation = Orientation.DOWN

    fun activate() {
        val minX = when (orientation) {
            Orientation.LEFT -> entity.boundingBoxComponent.centerWorld.x - entity.boundingBoxComponent.getWidth()
            Orientation.RIGHT -> entity.boundingBoxComponent.centerWorld.x + entity.boundingBoxComponent.getWidth()
            else -> entity.boundingBoxComponent.centerWorld.x
        }
        val minY = when (orientation) {
            Orientation.UP -> entity.boundingBoxComponent.centerWorld.y - entity.boundingBoxComponent.getHeight()
            Orientation.DOWN -> entity.boundingBoxComponent.centerWorld.y + entity.boundingBoxComponent.getHeight()
            else -> entity.boundingBoxComponent.centerWorld.y
        }
        val checkBox = Rectangle2D(minX, minY, entity.boundingBoxComponent.getWidth(), entity.boundingBoxComponent.getHeight())
        getGameWorld().getEntitiesInRange(checkBox)
                .filter { it.hasComponent(ActivateComponent::class.java) }
                .forEach {
                    it.getComponent<ActivateComponent>().activate()
                }
    }

    fun moveRight(): Boolean {
        orientation = Orientation.RIGHT
        return if (canMove(Point2D(speed.toDouble(), 0.0))) {
            position.translateX(speed.toDouble())
            true
        } else {
            false
        }
    }

    fun moveLeft(): Boolean {
        orientation = Orientation.LEFT
        return if (canMove(Point2D(speed.toDouble().withSign(-1), 0.0))) {
            position.translateX(speed.toDouble().withSign(-1))
            true
        } else {
            false
        }
    }

    fun moveUp(): Boolean {
        orientation = Orientation.UP
        return if (canMove(Point2D(0.0, speed.toDouble().withSign(-1)))) {
            position.translateY(speed.toDouble().withSign(-1))
            true
        } else {
            false
        }
    }

    fun moveDown(): Boolean {
        orientation = Orientation.DOWN
        return if (canMove(Point2D(0.0, speed.toDouble()))) {
            position.translateY(speed.toDouble())
            true
        } else {
            false
        }
    }

    private fun canMove(direction: Point2D): Boolean {
        val newPosition: Point2D = position.position.add(direction)
        val previousHitBox = entity.boundingBoxComponent.hitBoxesProperty().first()
        val nextBox = FXGL.entityBuilder()
                .at(previousHitBox.minXWorld + direction.x, previousHitBox.minYWorld + direction.y)
                .bbox(HitBox(BoundingShape.box(previousHitBox.width, previousHitBox.height)))
                .apply {
                    if (getGameState().getBoolean("debug")) {
                        this.view(Rectangle(32.0, 48.0, Color.BLACK).apply {
                            this.opacity = 0.2
                        })
                    }
                }
                .collidable()
                .buildAndAttach()
        val canMove = getGameScene().viewport.visibleArea.contains(newPosition)
                && getGameWorld().getCollidingEntities(nextBox).none {
            it.typeComponent.isType(EntityType.OBJECT) || it.typeComponent.isType(EntityType.NPC)
        }
        getGameWorld().removeEntity(nextBox)
        return canMove
    }

    enum class Orientation {
        UP, DOWN, LEFT, RIGHT
    }
}
