package com.github.wakingrufus.rpg.party

import com.github.wakingrufus.rpg.inventory.Armor
import com.github.wakingrufus.rpg.inventory.Weapon

class PartyMember(val name: String,
val maxHp: Int,
val speed: Int,
val armor: Armor? = null,
val weapon: Weapon? = null,
val skills: List<Skill>) {
}