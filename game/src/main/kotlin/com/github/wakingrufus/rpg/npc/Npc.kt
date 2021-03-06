package com.github.wakingrufus.rpg.npc

import com.almasb.fxgl.entity.SpawnData
import com.github.wakingrufus.rpg.entities.name
import com.github.wakingrufus.rpg.sprites.LPCSpriteSheet

class Npc(val name: String, val sprite: LPCSpriteSheet, val x: Double, val y: Double, val conversation: Conversation?) {
    fun spawnData(): SpawnData {
        return SpawnData(x, y).apply {
            name(name)
            put("sprite", sprite)
            conversation?.also {
                put("conversation",it)
            }
        }
    }
}

class NpcBuilder(val name: String, val sprite: LPCSpriteSheet, val x: Double, val y: Double) {
    private var conversation: Conversation? = null
    fun conversation(builder: ConversationBuilder.() -> Unit) {
        conversation = ConversationBuilder().apply(builder).build()
    }

    fun build(): Npc {
        return Npc(name, sprite, x, y, conversation)
    }
}

class Conversation(val steps: List<ConversationStep>)

class ConversationBuilder {
    private val steps: MutableList<ConversationStep> = mutableListOf()
    fun step(speaker: Speaker, text: String) {
        steps.add(ConversationStep(speaker, text))
    }

    fun build(): Conversation {
        return Conversation(steps)
    }
}

class ConversationStep(val speaker: Speaker, val text: String)

enum class Speaker {
    SELF, PLAYER, OTHER
}

