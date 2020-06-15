package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.area.AreaDb
import com.github.wakingrufus.rpg.npc.Speaker

object PRAXIS_AREAS : AreaDb(){

    val badlands = area("Badlands", "map.tmx"){
        spawner("Golem", "Golem_02", 500.0, 500.0, 200, 1, 10.0) {
            enemy("golem A", 100, "Golem_02", 10)
        }
        npc("Townsperson", "Golem_02", 200.0, 500.0) {
            conversation {
                step(Speaker.SELF, "Hi")
                step(Speaker.PLAYER, "Hi there")
            }
        }
        mapObject(350.0, 500.0, 40.0, 40.0)
    }
}
