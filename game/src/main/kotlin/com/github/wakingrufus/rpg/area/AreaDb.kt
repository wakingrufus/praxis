package com.github.wakingrufus.rpg.area

import com.github.wakingrufus.rpg.RpgDsl

@RpgDsl
open class AreaDb {
    private val areaMap: MutableMap<String, Area> = mutableMapOf()
    fun area(name: String, map: String, builder: AreaBuilder.() -> Unit): Area {
        return AreaBuilder(name, map).apply(builder).build().also {
            areaMap[name] = it
        }
    }

    fun getByName(name: String): Area? {
        return areaMap[name]
    }
}
