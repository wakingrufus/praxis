package com.github.wakingrufus.rpg.enemies

import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet

class Spawner(val name: String,
              val sprite: LPCSpriteSheet,
              val x: Double,
              val y: Double,
              val aggroRange: Int,
              val speed: Int,
              val respawnTime: Double,
              val party: EnemyParty)
