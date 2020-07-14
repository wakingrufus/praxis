package com.github.wakingrufus.praxis

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.getComponent
import com.github.wakingrufus.rpg.battle.consumableChoice
import com.github.wakingrufus.rpg.entities.EntityType
import com.github.wakingrufus.rpg.inventory.Consumable
import com.github.wakingrufus.rpg.inventory.InventoryComponent
import com.github.wakingrufus.rpg.party.SkillDb
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

object PraxisSkillDb : SkillDb() {
    val attack = skill("Attack") {
        activeAbility("Attack") {
            targetEffect { performer, target ->
                val attackType = performer.attackDamageType()
                val attackPower = performer.attackModifier(attackType)
                target.takeDamage(attackPower, attackType)
            }
        }
    }
    val pray = skill("Pray") {
        activeAbility("Pray") {
            animation { AttackAnimationType.SPELL }
            alliesEffect { performer, target -> target.heal(performer.attackModifier(HEAL)) }
        }
    }
    val nuke = skill("Nuke") {
        activeAbility("Nuke") {
            animation { AttackAnimationType.SPELL }
            enemiesEffect { performer, target -> target.takeDamage(50 + performer.attackModifier(FIRE), FIRE) }
        }
    }
    val item = skill("Item") {
        chooseChoice("Item") {
            getGameWorld().getEntitiesByType(EntityType.PLAYER).first()
                    .getComponent<InventoryComponent>().byName()
                    .mapNotNull {
                        when (val item = it.first) {
                            is Consumable -> consumableChoice(item.name + ": " + it.second, item, item.ability)
                            else -> null
                        }
                    }
        }
    }
}