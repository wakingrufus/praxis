package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.Weapon
import com.github.wakingrufus.rpg.battle.abilityChoice

@Required(AbilitiesComponent::class)
class WeaponComponent(val weapon: Weapon) : Component() {
    override fun onAdded() {
        entity.getComponent<AbilitiesComponent>().addAbility(
                abilityChoice("Attack") {
                    targetEffect { target -> target.takeDamage(weapon.damage, weapon.damageType) }
                })
    }
}
