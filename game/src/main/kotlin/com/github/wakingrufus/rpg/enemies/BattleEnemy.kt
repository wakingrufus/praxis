package com.github.wakingrufus.rpg.enemies

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.github.wakingrufus.rpg.battle.BattleAction
import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet

class BattleEnemy(val name: String, val maxHp: Int, val sprite: LPCSpriteSheet, val speed: Int,
                  val ai: (self: Entity, gameWorld: GameWorld) -> BattleAction)