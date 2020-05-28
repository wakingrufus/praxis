package com.github.wakingrufus.rpg

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.components.TransformComponent
import com.github.wakingrufus.rpg.entities.EntityType

@Required(TransformComponent::class)
class MonsterAggroComponent(val name: String, val enemies: List<SpawnData>) : Component() {
    @Override
    override fun onUpdate(tpf: Double) {
        val player = FXGL.Companion.getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
        if (distanceToPlayer(player) < 100) {
            getEntity().getComponent<TransformComponent>().translateTowards(player.position, 1.0)
        } else {
            // stop signalling
        }
    }

    private fun distanceToPlayer(player: Entity): Double {
        return player.getComponent<TransformComponent>().distance(getEntity().getComponent())
    }

}