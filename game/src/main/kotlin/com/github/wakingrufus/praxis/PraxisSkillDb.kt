package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.party.SkillDb
import com.github.wakingrufus.rpg.sprites.AttackAnimationType

object PraxisSkillDb : SkillDb(){
    val attack = skill("Attack"){
        activeAbility("Attack") {
            targetEffect { performer, target ->
                val attackType = performer.attackDamageType()
                val attackPower = performer.attackModifier(attackType)
                target.takeDamage(attackPower, attackType)
            }
        }
    }
    val pray = skill("Pray"){
        activeAbility("Pray") {
            animation { AttackAnimationType.SPELL }
            alliesEffect { performer, target -> target.heal(performer.attackModifier(HEAL)) }
        }
    }
}