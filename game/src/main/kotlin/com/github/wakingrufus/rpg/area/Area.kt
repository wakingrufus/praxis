package com.github.wakingrufus.rpg.area

import com.almasb.fxgl.entity.SpawnData
import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.enemies.EnemyPartyBuilder
import com.github.wakingrufus.rpg.enemies.Spawner
import com.github.wakingrufus.rpg.npc.Npc
import com.github.wakingrufus.rpg.npc.NpcBuilder
import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet
import javafx.scene.paint.Color

data class Area(
        val name: String,
        val map: String,
        val spawners: List<Spawner>,
        val objects: List<SpawnData>,
        val npcs: List<Npc>)

@RpgDsl
class AreaBuilder(val name: String, val map: String) {
    val spawners: MutableList<Spawner> = mutableListOf()
    val objects: MutableList<SpawnData> = mutableListOf()
    val npcs: MutableList<Npc> = mutableListOf()

    fun spawner(name: String, sprite: LPCSpriteSheet, x: Double, y: Double,
                aggroRange: Int, speed: Int,
                respawnTime: Double,
                party: EnemyPartyBuilder.() -> Unit) {
        spawners.add(Spawner(name, sprite, x, y, aggroRange, speed, respawnTime, EnemyPartyBuilder().apply(party).build()))
    }

    fun npc(name: String, sprite: LPCSpriteSheet, x: Double, y: Double, builder: NpcBuilder.() -> Unit): Npc {
        return NpcBuilder(name, sprite, x, y).apply(builder).build().also {
            npcs.add(it)
        }
    }

    fun mapObject(x: Double, y: Double, width: Double, height: Double) {
        objects.add(SpawnData(x, y).apply {
            put("width", width)
            put("height", height)
            put("color", Color.BROWN)
        })
    }

    fun build(): Area {
        return Area(name, map, spawners, objects, npcs)
    }
}