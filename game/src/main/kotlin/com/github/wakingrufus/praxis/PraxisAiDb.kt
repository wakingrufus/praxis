package com.github.wakingrufus.praxis

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.github.wakingrufus.rpg.battle.AbilityActionChoice
import com.github.wakingrufus.rpg.battle.BattleAction
import com.github.wakingrufus.rpg.battle.battleComponent
import com.github.wakingrufus.rpg.entities.EntityType

object PraxisAiDb {
    val singleAbilityRandomTarget: (AbilityActionChoice) -> (Entity, GameWorld) -> BattleAction = { ability ->
        { self: Entity, gameWorld: GameWorld ->
            ability.toBattleAction(performer = self.battleComponent(),
                    target = gameWorld.getEntitiesByType(EntityType.PARTY).random().battleComponent(),
                    enemies = gameWorld.getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                    allies = gameWorld.getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
        }
    }
}