package com.github.wakingrufus.praxis

import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.rpg.battle.AbilityActionChoice
import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.battle.battleComponent
import com.github.wakingrufus.rpg.enemies.AbilitiesDb
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

object PraxisAbilitiesDb : AbilitiesDb() {
    private val log = Logger.get(javaClass)
    val attack: (amt: Int, damageType: DamageType, animationType: AttackAnimationType) -> AbilityActionChoice = { amt, type, animation ->
        ability("attack") {
            animation { animation }
            targetEffect { performer, target ->
                target.takeDamage(amt, type)
            }
            performerEffect {
                this@PraxisAbilitiesDb.log.info("${it.entity.battleComponent().name} attacks for $amt $type damage")
            }
        }
    }
}