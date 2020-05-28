package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getEventBus
import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.SpawnData
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.logging.Logger
import javafx.event.EventHandler

class SpawnerComponent(val spawnData: SpawnData) : Component() {
    private val log = Logger.get(javaClass)
    private var spawnedEntity: Entity? = null
    private var respawnTimer: Double = 0.0

    lateinit var handler: EventHandler<MonsterDespawnEvent>

    @Override
    override fun onUpdate(tpf: Double) {
    //    if (spawnedEntity?.isActive != true) {
            respawnTimer += tpf
            if (respawnTimer > spawnData.get<Double>("respawnTime")) {
                respawnTimer = 0.0
                log.info("spawning new ${spawnData.get<String>("name")}")
                spawnedEntity = getGameWorld().spawn("enemy", spawnData)
                pause()
                getEventBus().addEventHandler(MonsterDespawnEvent.ANY, handler)
            }
    //    }
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