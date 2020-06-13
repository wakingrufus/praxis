package com.github.wakingrufus.rpg.battle.ability

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.AbilityActionChoice
import com.github.wakingrufus.rpg.battle.chooseChoice
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.Consumable
import com.github.wakingrufus.rpg.inventory.InventoryComponent

@Required(AbilitiesComponent::class)

class ItemComponent : Component() {
    override fun onAdded() {
        entity.getComponent<AbilitiesComponent>().addAbility(
                chooseChoice("Item") {
                    getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
                            .getComponent<InventoryComponent>().byName()
                            .mapNotNull {
                                when (val item = it.first) {
                                    is Consumable -> AbilityActionChoice(item.name + ": " + it.second, item.ability)
                                    else -> null
                                }
                            }
                })
    }
}
