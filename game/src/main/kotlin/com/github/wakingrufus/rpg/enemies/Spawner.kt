package com.github.wakingrufus.rpg.enemies

class Spawner(val name: String,
              val sprite: String,
              val x: Double,
              val y: Double,
              val aggroRange: Int,
              val speed: Int,
              val respawnTime: Double,
              val party: BattleParty)
