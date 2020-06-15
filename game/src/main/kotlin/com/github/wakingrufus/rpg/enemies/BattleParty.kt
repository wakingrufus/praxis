package com.github.wakingrufus.rpg.enemies

import com.almasb.fxgl.entity.SpawnData
import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.entities.name

class BattleParty(val enemies: List<BattleEnemy>) {
    fun spawnData(): List<SpawnData> {
        return enemies.map {
            SpawnData(1440.0, 200.0 + (enemies.size * 100)).apply {
                name(it.name)
                put("maxHp", it.maxHp)
                put("sprite", it.sprite)
                put("speed", it.speed)
            }
        }
    }
}

@RpgDsl
class BattlePartyBuilder {
    val enemies: MutableList<BattleEnemy> = mutableListOf()
    fun enemy(name: String, maxHp: Int, sprite: String, speed: Int): BattleEnemy {
        return BattleEnemy(name, maxHp, sprite, speed).also {
            enemies.add(it)
        }
    }

    fun build(): BattleParty {
        return BattleParty(enemies)
    }
}
