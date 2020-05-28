package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.component.Component

class HealPowerComponent(private val baseHealPower: Int) : Component() {
    private var modifier: Int = 0
    fun effectiveHealPower(): Int {
        return baseHealPower + modifier
    }

    fun modifyHealPower(amount: Int) {
        modifier += amount
    }
}
