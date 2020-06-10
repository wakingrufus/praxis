package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.components.TransformComponent
import com.almasb.fxgl.entity.components.TypeComponent
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.geometry.Point2D
import javafx.geometry.Rectangle2D
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.withSign

class FieldMovementComponent(val speed: Int) : Component() {

    private lateinit var position: TransformComponent

    fun moveRight() : Boolean {
        return if (canMove(Point2D(speed.toDouble(), 0.0))) {
            position.translateX(speed.toDouble())
            true
        } else {
            false
        }
    }

    fun moveLeft() : Boolean{
        return if (canMove(Point2D(speed.toDouble().withSign(-1), 0.0))) {
            position.translateX(speed.toDouble().withSign(-1))
            true
        } else {
            false
        }
    }

    fun moveUp() : Boolean{
        return if (canMove(Point2D(0.0, speed.toDouble().withSign(-1)))) {
            position.translateY(speed.toDouble().withSign(-1))
            true
        }else {
            false
        }
    }

    fun moveDown(): Boolean {
       return  if (canMove(Point2D(0.0, speed.toDouble()))) {
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
                        .none { type -> type.isType(EntityType.OBJECT) }
    }
}