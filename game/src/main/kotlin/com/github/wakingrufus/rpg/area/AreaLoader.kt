package com.github.wakingrufus.rpg.area

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.entity.GameWorld

class AreaLoader(val gameWorld: GameWorld) {

    fun loadArea(name: String) {
        areaData[name]?.let { area ->
            FXGL.setLevelFromMap(area.map)
            area.objects.forEach {
                gameWorld.spawn("rectangle",it)
            }
            area.spawners.forEach {
                gameWorld.spawn("spawner",it)
            }
        }
    }
}