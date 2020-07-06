package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getEventBus
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.enemies.Spawner
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.event.EventHandler

class SpawnerComponent(val spawner: Spawner) : Component() {
    private val log = Logger.get(javaClass)
    private var spawnedEntity: Entity? = null
    private var respawnTimer: Double = 0.0

    @Override
    override fun onUpdate(tpf: Double) {
        respawnTimer += tpf
        if (respawnTimer > spawner.respawnTime) {
            respawnTimer = 0.0
            log.info("spawning new ${spawner.name}")
            spawnedEntity = FXGL.entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(spawner.x, spawner.y)
                    .bbox(HitBox(BoundingShape.box(32.0, 56.0)))
                    .collidable()
                    .with(FieldComponent())
                    .with(FieldLpcAnimationComponent(spawner.sprite))
                    .with(FieldMovementComponent(spawner.speed))
                    .with(MonsterAggroComponent(spawner.name, spawner.aggroRange, spawner.party))
                    .buildAndAttach()
        }
    }
}
