package com.github.wakingrufus.praxis

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.GameWorld
import com.almasb.fxgl.entity.getComponent
import com.almasb.fxgl.logging.Logger
import com.github.wakingrufus.praxis.PraxisItemDb.Potion
import com.github.wakingrufus.rpg.area.AreaDb
import com.github.wakingrufus.rpg.battle.BattleAction
import com.github.wakingrufus.rpg.battle.BattleAnimationComponent
import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.battle.battleComponent
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.npc.Speaker

object PRAXIS_AREAS : AreaDb() {
    private val log = Logger.get(javaClass)

    val badlands = area("Badlands", "map.tmx") {
        spawner("Golem", "man1", 500.0, 500.0, 200, 1, 10.0) {
            val attack: (amt: Int, type: DamageType) -> (Entity, GameWorld) -> BattleAction = { amt, type ->
                { self: Entity, gameWorld: GameWorld ->
                    ability("attack") {
                        targetEffect {
                            it.takeDamage(amt, type)
                            this@PRAXIS_AREAS.log.info("${self.battleComponent().name} attacks for $amt $type damage")
                        }
                        performerEffect { it.entity.getComponent<BattleAnimationComponent>().attack() }
                    }.toBattleAction(performer = self.battleComponent(),
                            target = gameWorld.getEntitiesByType(EntityType.PARTY).random().battleComponent(),
                            enemies = gameWorld.getEntitiesByType(EntityType.PARTY).map { it.battleComponent() },
                            allies = gameWorld.getEntitiesByType(EntityType.ENEMY_PARTY).map { it.battleComponent() })
                }
            }
            enemy("golem A", 100, "man1", 10, attack(3, DamageType.MELEE))
            loot {
                item(90, Potion)
                item(10, Potion)
            }
        }
        npc("Townsperson", "man1", 200.0, 500.0) {
            conversation {
                step(Speaker.SELF, "Hi")
                step(Speaker.PLAYER, "Hi there")
            }
        }
        mapObject(350.0, 500.0, 40.0, 40.0)
    }
}
