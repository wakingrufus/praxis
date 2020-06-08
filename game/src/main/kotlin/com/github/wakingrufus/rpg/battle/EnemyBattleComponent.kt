package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.almasb.fxgl.entity.component.Required

@Required(BattleComponent::class)
class EnemyBattleComponent() : Component() {

    @Override
    override fun onUpdate(tpf: Double) {
        if(entity.battleComponent().currentHp <= 0){
            getGameWorld().removeEntity(this.entity)
        }
    }
}
