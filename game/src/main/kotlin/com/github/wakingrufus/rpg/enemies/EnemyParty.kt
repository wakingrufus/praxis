package com.github.wakingrufus.rpg.enemies

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.BattleAction
import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet

class EnemyParty(val enemies: List<BattleEnemy>, val lootTable: LootTable)

@RpgDsl
class EnemyPartyBuilder {
    private val enemies: MutableList<BattleEnemy> = mutableListOf()
    private val lootTableBuilder = LootTableBuilder()

    fun enemy(name: String,
              maxHp: Int,
              sprite: LPCSpriteSheet,
              speed: Int,
              ai: (self: Entity, gameWorld: GameWorld) -> BattleAction): BattleEnemy {
        return BattleEnemy(name, maxHp, sprite, speed, ai).also {
            enemies.add(it)
        }
    }

    fun loot(builder: LootTableBuilder.() -> Unit) {
        lootTableBuilder.apply(builder)
    }

    fun build(): EnemyParty {
        return EnemyParty(enemies, lootTableBuilder.build())
    }
}
