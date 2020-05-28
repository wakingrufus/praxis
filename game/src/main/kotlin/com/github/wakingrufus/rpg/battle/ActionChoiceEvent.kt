package com.github.wakingrufus.rpg.battle

import javafx.event.Event
import javafx.event.EventType

class ActionChoiceEvent(eventType: EventType<out ActionChoiceEvent>, val choice: Int) : Event(eventType){
    companion object {
        @JvmField val ANY = EventType<ActionChoiceEvent>(Event.ANY, "ACTION_CHOICE_EVENT")
    }
}
