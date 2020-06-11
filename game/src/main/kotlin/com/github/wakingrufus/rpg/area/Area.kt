package com.github.wakingrufus.rpg.area

import com.almasb.fxgl.entity.SpawnData
import com.github.wakingrufus.rpg.entities.BattlePartyBuilder
import com.github.wakingrufus.rpg.entities.battleParty
import com.github.wakingrufus.rpg.entities.name
import com.github.wakingrufus.rpg.npc.Npc
import com.github.wakingrufus.rpg.npc.NpcBuilder
import javafx.scene.paint.Color

data class Area(
        val name: String,
        val map: String,
        val spawners: List<SpawnData>,
        val objects: List<SpawnData>,
        val npcs: List<Npc>)

fun area(name: String, map: String, builder: AreaBuilder.() -> Unit): Area {
    return AreaBuilder(name, map).apply(builder).build()
}


class AreaBuilder(val name: String, val map: String) {
    val spawners: MutableList<SpawnData> = mutableListOf()
    val objects: MutableList<SpawnData> = mutableListOf()
    val npcs: MutableList<Npc> = mutableListOf()

    fun spawn(name: String, sprite: String, x: Double, y: Double,
              aggroRange: Int, speed: Int,
              respawnTime: Double,
              party: BattlePartyBuilder.() -> Unit) {
        spawners.add(SpawnData(x, y).apply {
            this.name(name)
            put("sprite", sprite)
            put("aggroRange", aggroRange)
            put("speed", speed)
            battleParty(party)
            put("respawnTime", respawnTime)
        })
    }

    fun npc(name: String, sprite: String, x: Double, y: Double, builder: NpcBuilder.() -> Unit): Npc {
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