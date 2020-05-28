package com.github.wakingrufus.rpg

import com.almasb.fxgl.core.collection.PropertyMap
import com.almasb.fxgl.input.Input
import javafx.scene.input.KeyCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class MenuActionTest {
    @Test
    fun `test openMenu`() {
        var callOpen = false
        var callExit = false
        val gameState = PropertyMap().apply {
            this.setValue("menu",false)
        }
        val instance = MenuAction(gameState, { callOpen = true }, { callExit = true })
        val input = Input()
        input.addAction(instance, KeyCode.K)
        input.mockKeyPress(KeyCode.K)
        input.mockKeyRelease(KeyCode.K)
        assertThat(callOpen).`as`("open menu function is called").isTrue()
        assertThat(callExit).`as`("exit menu function is not called").isFalse()
        assertThat(gameState.getValueOptional<Boolean>("menu")).hasValue(true)
    }

    @Test
    fun `test closeMenu`() {
        var callOpen = false
        var callExit = false
        val gameState = PropertyMap().apply {
            this.setValue("menu",true)
        }
        val instance = MenuAction(gameState, { callOpen = true }, { callExit = true })
        val input = Input()
        input.addAction(instance, KeyCode.K)
        input.mockKeyPress(KeyCode.K)
        input.mockKeyRelease(KeyCode.K)
        assertThat(callOpen).isFalse()
        assertThat(callExit).isTrue()
        assertThat(gameState.getValueOptional<Boolean>("menu"))
                .`as`("Doesn't change state")
                .hasValue(true)
    }
}
