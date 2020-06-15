package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.component.RequiredComponents
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.enemies.BattleParty
import com.github.wakingrufus.rpg.entities.EntityType

@RequiredComponents(
        Required(FieldMovementComponent::class),
        Required(FieldAnimationComponent::class))
class MonsterAggroComponent(val name: String, val aggroRange: Int, val enemies: BattleParty) : Component() {
    @Override
    override fun onUpdate(tpf: Double) {
        val player = FXGL.Companion.getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
        if (distanceToPlayer(player) < aggroRange) {
            if (entity.boundingBoxComponent.centerWorld.x < player.boundingBoxComponent.centerWorld.x) {
                entity.getComponent<FieldAnimationComponent>().faceRight()
                entity.getComponent<FieldMovementComponent>().moveRight()
            } else if (entity.boundingBoxComponent.centerWorld.x > player.boundingBoxComponent.centerWorld.x) {
                entity.getComponent<FieldAnimationComponent>().faceLeft()
                entity.getComponent<FieldMovementComponent>().moveLeft()
            }
            if (entity.boundingBoxComponent.centerWorld.y < player.boundingBoxComponent.centerWorld.y) {
                entity.getComponent<FieldMovementComponent>().moveDown()
            } else if (entity.boundingBoxComponent.centerWorld.y > player.boundingBoxComponent.centerWorld.y) {
                entity.getComponent<FieldMovementComponent>().moveUp()
            }
            entity.getComponent<FieldAnimationComponent>().walk()
        } else {
            // stop signalling
        }
    }

    private fun distanceToPlayer(player: Entity): Double {
        return entity.distanceBBox(player)
    }
}
