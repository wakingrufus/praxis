package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.Weapon
import com.github.wakingrufus.rpg.battle.action

@Required(AbilitiesComponent::class)
class WeaponComponent(val weapon: Weapon) : Component() {
    override fun onAdded() {
        entity.getComponent<AbilitiesComponent>().addAbility(
                action("Attack") { targetEffect { target -> target.takeDamage(weapon.damage, weapon.damageType) } })
    }
}
