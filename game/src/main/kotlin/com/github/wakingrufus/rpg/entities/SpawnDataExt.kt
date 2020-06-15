package com.github.wakingrufus.rpg.entities

import com.almasb.fxgl.entity.SpawnData

fun SpawnData.name(name: String) {
    put("name", name)
}