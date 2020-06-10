package com.github.wakingrufus.rpg.entities

import com.almasb.fxgl.entity.SpawnData

fun SpawnData.name(name: String) {
    put("name", name)
}

fun SpawnData.battleParty(enemies: BattlePartyBuilder.() -> Unit) {
    put("party", BattlePartyBuilder().apply(enemies).enemies)
}

class BattlePartyBuilder {
    val enemies: MutableList<SpawnData> = mutableListOf()
    fun enemy(name: String, maxHp: Int, sprite: String, speed: Int): SpawnData {
        return SpawnData(1440.0, 200.0 + (enemies.size * 100)).apply {
            name(name)
            put("maxHp", maxHp)
            put("sprite", sprite)
            put("speed", speed)
        }.also {
            enemies.add(it)
        }
    }
}
