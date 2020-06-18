package com.github.wakingrufus.rpg.status

import com.github.wakingrufus.rpg.RpgDsl

@RpgDsl
open class StatusEffectDb {
    fun statusEffect(name: String, builder: StatusEffectBuilder.() -> Unit): StatusEffect{
        return StatusEffectBuilder(name).apply(builder).build()
    }
}