package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.TransformComponent
import com.almasb.fxgl.entity.components.TypeComponent
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import kotlin.math.min
import kotlin.math.withSign

class FieldMovementComponent(val speed: Int) : Component() {

    private lateinit var position: TransformComponent
    private var orientation: Orientation = Orientation.DOWN

    fun activate() {
        val minX = when (orientation) {
            Orientation.LEFT -> entity.x - entity.boundingBoxComponent.getWidth()
            Orientation.RIGHT -> entity.x + entity.boundingBoxComponent.getWidth()
            else -> entity.x
        }
        val minY = when (orientation) {
            Orientation.UP -> entity.y - entity.boundingBoxComponent.getHeight()
            Orientation.DOWN -> entity.y + entity.boundingBoxComponent.getHeight()
            else -> entity.y
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
        val minX = min(position.x, position.x + direction.x)
        val width = direction.x + entity.boundingBoxComponent.getWidth()
        val minY = min(position.y, position.y + direction.y)
        val height = direction.y + entity.boundingBoxComponent.getHeight()
        val newPosition: Point2D = position.position.add(direction)

        return FXGL.getGameScene().viewport.visibleArea.contains(newPosition) &&
                FXGL.getGameWorld().getEntitiesInRange(Rectangle2D(minX, minY, width, height))
                        .map { it.getComponent<TypeComponent>() }
                        .none { type -> type.isType(EntityType.OBJECT) || type.isType(EntityType.NPC) }
    }

    enum class Orientation {
        UP, DOWN, LEFT, RIGHT
    }
}
