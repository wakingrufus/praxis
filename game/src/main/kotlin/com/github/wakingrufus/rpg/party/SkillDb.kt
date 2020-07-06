package com.github.wakingrufus.rpg.party

import com.github.wakingrufus.rpg.RpgDsl

@RpgDsl
open class SkillDb {
    fun skill(name: String, builder: SkillBuilder.() -> Unit): Skill {
        return SkillBuilder(name).apply(builder).build()
    }
}
