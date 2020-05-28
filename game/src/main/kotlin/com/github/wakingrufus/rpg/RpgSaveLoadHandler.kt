package com.github.wakingrufus.rpg

import com.almasb.fxgl.core.serialization.Bundle
import com.almasb.fxgl.dsl.FXGL.Companion.getWorldProperties
import com.almasb.fxgl.dsl.getGameState
import com.almasb.fxgl.profile.DataFile
import com.almasb.fxgl.profile.SaveFile
import com.almasb.fxgl.profile.SaveLoadHandler
import java.time.LocalDateTime

class RpgSaveLoadHandler : SaveLoadHandler {
    override fun onLoad(data: DataFile) {
        getGameState().setValue("menu", data.getBundle("Hello").get("menu"))
        getWorldProperties().setValue("map", data.getBundle("Hello").get("map"))
    }

    override fun onSave(data: DataFile) {
        val bundle1 = Bundle("Hello")
        bundle1.put("menu", getGameState().getString("menu"))
        bundle1.put("map", getWorldProperties().getString("map"))
        bundle1.put("id", 9)

        val data1 = DataFile()
        data1.putBundle(bundle1)
    }
}