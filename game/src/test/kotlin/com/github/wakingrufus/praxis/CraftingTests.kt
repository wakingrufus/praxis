package com.github.wakingrufus.praxis


import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.github.wakingrufus.praxis.PraxisItemDb.Iron
import com.github.wakingrufus.praxis.PraxisItemDb.Oak
import com.github.wakingrufus.praxis.PraxisItemDb.Topaz
import com.github.wakingrufus.praxis.PraxisRecipesDb.ShortSword
import com.github.wakingrufus.rpg.battle.DamageType
import com.github.wakingrufus.rpg.inventory.EquipmentSlot
import org.junit.jupiter.api.Test

internal class CraftingTests {
    @Test
    fun test() {
        assertThat(ShortSword.canCraft(listOf(Iron, Iron, Iron, Oak, Topaz))).isTrue()
        val shortSword = ShortSword.craft(listOf(Iron, Iron, Iron, Oak, Topaz))
        assertThat(shortSword.slot).isEqualTo(EquipmentSlot.WEAPON)
        assertThat(shortSword.primaryDamageType).isEqualTo(DamageType.MELEE)
    }
}
