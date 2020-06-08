package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.component.RequiredComponents
import com.almasb.fxgl.entity.components.TransformComponent
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.entities.EntityType

@RequiredComponents(
        Required(TransformComponent::class),
        Required(FieldAnimationComponent::class))
class MonsterAggroComponent(val name: String, val enemies: List<SpawnData>) : Component() {
    @Override
    override fun onUpdate(tpf: Double) {
        val player = FXGL.Companion.getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
        if (distanceToPlayer(player) < 100) {
            entity.getComponent<TransformComponent>().translateTowards(player.position, 1.0)
            if (entity.position.x > player.position.x) {
                entity.getComponent<FieldAnimationComponent>().faceLeft()
            } else {
                entity.getComponent<FieldAnimationComponent>().faceRight()
            }
            entity.getComponent<FieldAnimationComponent>().walk()
        } else {
            // stop signalling
        }
    }

    private fun distanceToPlayer(player: Entity): Double {
        return player.getComponent<TransformComponent>().distance(getEntity().getComponent())
    }
}
