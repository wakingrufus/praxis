package com.github.wakingrufus.rpg.enemies

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.github.wakingrufus.rpg.battle.BattleAction

class BattleEnemy(val name: String, val maxHp: Int, val sprite: String, val speed: Int,
                  val ai: (self: Entity, gameWorld: GameWorld)-> BattleAction)