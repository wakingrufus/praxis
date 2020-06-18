package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required

@Required(BattleComponent::class)
class BattleAiComponent(val ai: (self: Entity, gameWorld: GameWorld) -> BattleAction) : Component() {

    @Override
    override fun onUpdate(tpf: Double) {
        if (entity.battleComponent().queueIsEmpty()) {
            entity.battleComponent().queueAction(ai(this.entity, getGameWorld()))
        }
    }
}
