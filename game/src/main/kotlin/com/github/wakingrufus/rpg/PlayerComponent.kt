package com.github.wakingrufus.rpg

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.TransformComponent
import com.almasb.fxgl.entity.components.TypeComponent
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.geometry.Point2D
import kotlin.math.withSign

class PlayerComponent : Component() {

    private lateinit var position: TransformComponent
    private var speed: Int = 2

    fun moveRight() {
        if (canMove(Point2D(speed.toDouble(), 0.0))) {
            position.translateX(speed.toDouble())
        }
    }

    fun moveLeft() {
        if (canMove(Point2D(speed.toDouble().withSign(-1), 0.0))) {
            position.translateX(speed.toDouble().withSign(-1))
        }
    }

    fun moveUp() {
        if (canMove(Point2D(0.0, speed.toDouble().withSign(-1)))) {
            position.translateY(speed.toDouble().withSign(-1))
        }
    }

    fun moveDown() {
        if (canMove(Point2D(0.0, speed.toDouble())))
            position.translateY(speed.toDouble())
    }

    private fun canMove(direction: Point2D): Boolean {
        val newPosition: Point2D = position.position.add(direction)

        return FXGL.getGameScene().viewport.visibleArea.contains(newPosition) &&
                FXGL.getGameWorld()
                        .getEntitiesAt(newPosition)
                        .filter { it.hasComponent(TypeComponent::class.java) }
                        .map { it.getComponent<TypeComponent>() }
                        .none { type -> type.isType(EntityType.OBJECT) }
    }
}