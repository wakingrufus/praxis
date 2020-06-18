package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.status.StatusEffectDb

object PraxisStatusEffectDb : StatusEffectDb(){
    val Burn = statusEffect("Burn") {
        periodic { takeDamage(1, DamageType.FIRE) }
    }
}