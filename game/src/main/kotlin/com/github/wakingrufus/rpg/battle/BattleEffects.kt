package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.logging.Logger

object BattleEffects {
    private val log = Logger.get(javaClass)
    val noopAttack: (Entity, List<Entity>) -> Unit = { performer, targets ->
        log.info("${performer} attacks $targets")
    }
    val attack: (Entity, List<Entity>) -> Unit = { performer, targets ->
        log.info("${performer} attacks $targets")
    }
}