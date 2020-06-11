package com.github.wakingrufus.rpg.area

import com.github.wakingrufus.rpg.npc.Speaker

val areaData = mapOf(
        "home" to area("Home", "map.tmx") {
            spawn("Golem", "Golem_02", 500.0, 500.0, 200, 1, 10.0) {
                enemy("golem A", 100, "Golem_02", 10)
            }
            npc("Townsperson", "Golem_02", 200.0, 500.0) {
                conversation {
                    step(Speaker.SELF, "Hi")
                }
            }
            mapObject(350.0, 500.0, 40.0, 40.0)
        }
)