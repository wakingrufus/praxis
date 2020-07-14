package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.dsl.getbp
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.BattleStateKeys
import com.github.wakingrufus.rpg.entities.EntityType

fun movePlayerNorth() {
    if(!getbp(BattleStateKeys.inBattle).value) {
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldMovementComponent>().moveUp()
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldLpcAnimationComponent>().walkNorth()
        FXGL.getWorldProperties().increment("pixelsMoved", +5)
    }
}

fun movePlayerSouth() {
    if(!getbp(BattleStateKeys.inBattle).value) {
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldMovementComponent>().moveDown()
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldLpcAnimationComponent>().walkSouth()
        FXGL.getWorldProperties().increment("pixelsMoved", +5)
    }
}

fun movePlayerWest() {
    if(!getbp(BattleStateKeys.inBattle).value) {
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldMovementComponent>().moveLeft()
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldLpcAnimationComponent>().walkWest()
        FXGL.getWorldProperties().increment("pixelsMoved", +5)
    }
}

fun movePlayerEast() {
    if(!getbp(BattleStateKeys.inBattle).value) {
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldMovementComponent>().moveRight()
        getGameWorld().getEntitiesByType(EntityType.PLAYER).first().getComponent<FieldLpcAnimationComponent>().walkEast()
        FXGL.getWorldProperties().increment("pixelsMoved", +5)
    }
}