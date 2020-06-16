package com.github.wakingrufus.rpg.enemies

import com.github.wakingrufus.rpg.RpgDsl

class BattleParty(val enemies: List<BattleEnemy>, val lootTable: LootTable)

@RpgDsl
class BattlePartyBuilder {
    private val enemies: MutableList<BattleEnemy> = mutableListOf()
    private val lootTableBuilder = LootTableBuilder()

    fun enemy(name: String, maxHp: Int, sprite: String, speed: Int): BattleEnemy {
        return BattleEnemy(name, maxHp, sprite, speed).also {
            enemies.add(it)
        }
    }

    fun loot(builder: LootTableBuilder.() -> Unit) {
        lootTableBuilder.apply(builder)
    }

    fun build(): BattleParty {
        return BattleParty(enemies, lootTableBuilder.build())
    }
}
