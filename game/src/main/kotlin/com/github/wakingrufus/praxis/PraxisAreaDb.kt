package com.github.wakingrufus.praxis

import com.github.wakingrufus.praxis.PraxisAbilitiesDb.attack
import com.github.wakingrufus.praxis.PraxisAiDb.singleAbilityRandomTarget
import com.github.wakingrufus.praxis.PraxisItemDb.Potion
import com.github.wakingrufus.praxis.PraxisSpriteDb.man1
import com.github.wakingrufus.praxis.PraxisSpriteDb.skeletonWithBow
import com.github.wakingrufus.rpg.area.AreaDb
import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.npc.Speaker
import com.github.wakingrufus.rpg.sprites.AttackAnimationType
import com.github.wakingrufus.rpg.sprites.characterSpriteSheet

object PraxisAreaDb : AreaDb() {
    val badlands = area("Badlands", "map.tmx") {
        spawner("Skeleton Archer", skeletonWithBow, 500.0, 500.0, 200, 1, 10.0) {
            enemy("Skeleton Archer A", 100, skeletonWithBow, 10,
                    singleAbilityRandomTarget(attack(3, DamageType.RANGE, AttackAnimationType.SHOOT)))
            loot {
                item(90, Potion)
                item(10, Potion)
            }
        }
        npc("Townsperson", characterSpriteSheet(man1), 200.0, 500.0) {
            conversation {
                step(Speaker.SELF, "Hi")
                step(Speaker.PLAYER, "Hi there")
            }
        }
        mapObject(350.0, 500.0, 40.0, 40.0)
    }
}
