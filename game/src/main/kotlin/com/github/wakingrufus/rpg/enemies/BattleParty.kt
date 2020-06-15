package com.github.wakingrufus.rpg.enemies

import com.github.wakingrufus.rpg.RpgDsl

class BattleParty(val enemies: List<BattleEnemy>)

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
