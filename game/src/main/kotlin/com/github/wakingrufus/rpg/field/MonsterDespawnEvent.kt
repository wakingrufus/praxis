package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.achievement.AchievementEvent
import com.almasb.fxgl.entity.Entity
import javafx.event.Event
import javafx.event.EventType

class MonsterDespawnEvent(eventType: EventType<out MonsterDespawnEvent>, val entity: Entity) : Event(eventType){
    companion object {
        @JvmField val ANY = EventType<MonsterDespawnEvent>(Event.ANY, "MONSTER_DESPAN_EVENT")
    }
}
