package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.dsl.getEventBus
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.logging.Logger
import com.almasb.fxgl.physics.BoundingShape
import com.almasb.fxgl.physics.HitBox
import com.github.wakingrufus.rpg.entities.EntityType
import javafx.event.EventHandler

class SpawnerComponent(val spawnData: SpawnData) : Component() {
    private val log = Logger.get(javaClass)
    private var spawnedEntity: Entity? = null
    private var respawnTimer: Double = 0.0

    lateinit var handler: EventHandler<MonsterDespawnEvent>

    @Override
    override fun onUpdate(tpf: Double) {
        respawnTimer += tpf
        if (respawnTimer > spawnData.get<Double>("respawnTime")) {
            respawnTimer = 0.0
            log.info("spawning new ${spawnData.get<String>("name")}")
            spawnedEntity = FXGL.entityBuilder()
                    .type(EntityType.ENEMY)
                    .at(spawnData.x, spawnData.y)
                    .bbox(HitBox(BoundingShape.box(40.0, 40.0)))
                    .collidable()
                    .with(FieldAnimationComponent(spawnData.get("sprite")))
                    .with(MonsterAggroComponent(spawnData.get("name"), spawnData.get("party")))
                    .buildAndAttach()
            pause()
            getEventBus().addEventHandler(MonsterDespawnEvent.ANY, handler)
        }
    }

    init {
        handler = EventHandler<MonsterDespawnEvent> { event: MonsterDespawnEvent ->
            if (spawnedEntity == event.entity) {
                log.info("resuming spawner ${spawnData.get<String>("name")}")
                getEventBus().removeEventHandler(MonsterDespawnEvent.ANY, handler)
                respawnTimer = 0.0
                resume()
            }
        }
    }
}