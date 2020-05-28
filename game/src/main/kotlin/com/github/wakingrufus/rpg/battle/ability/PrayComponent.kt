package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.component.RequiredComponents
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.HealPowerComponent
import com.github.wakingrufus.rpg.battle.action

@RequiredComponents(
        Required(AbilitiesComponent::class),
        Required(HealPowerComponent::class))
class PrayComponent : Component() {
    override fun onAdded() {
        entity.getComponent<AbilitiesComponent>().addAbility(
                action("Pray") {
                    alliesEffect { target -> target.heal(entity.getComponent<HealPowerComponent>().effectiveHealPower()) }
                }
        )
    }
}
