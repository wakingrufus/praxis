package com.github.wakingrufus.praxis

import com.github.wakingrufus.praxis.PraxisItemDb.Potion
import com.github.wakingrufus.rpg.area.AreaDb
import com.github.wakingrufus.rpg.npc.Speaker

object PRAXIS_AREAS : AreaDb(){

    val badlands = area("Badlands", "map.tmx"){
        spawner("Golem", "man1", 500.0, 500.0, 200, 1, 10.0) {
            enemy("golem A", 100, "man1", 10)
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
