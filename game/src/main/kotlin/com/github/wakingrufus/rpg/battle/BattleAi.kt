package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.entities.EntityType

interface BattleAi {
    fun decideNextAction(self: Entity, gameWorld: GameWorld): BattleAction
}

class Attacker : BattleAi {

    override fun decideNextAction(self: Entity, gameWorld: GameWorld): BattleAction {
        val action = abilityChoice("attack") {
            targetEffect { it.takeDamage(1, DamageType.MELEE) }
            performerEffect { it.entity.getComponent<BattleAnimationComponent>().attack() }
        }
        return action.toBattleAction(performer = self.battleComponent(),
                target = gameWorld.getEntitiesByType(EntityType.PARTY).random().battleComponent(),
                enemies = gameWorld.getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                allies = gameWorld.getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
    }
}