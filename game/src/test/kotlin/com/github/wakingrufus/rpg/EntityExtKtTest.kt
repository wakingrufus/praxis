package com.github.wakingrufus.rpg

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class EntityExtTest {
    private lateinit var entity: Entity

    @BeforeEach
    fun setUp() {
        entity = Entity()
    }

    @Test
    fun `Get component with extension`() {
        val comp = TestComponent()
        entity.addComponent(comp)

        val actual : TestComponent = entity.getComponent()
        assertThat(actual).isEqualTo(comp)
    }

    private inner class TestComponent : Component()
}
