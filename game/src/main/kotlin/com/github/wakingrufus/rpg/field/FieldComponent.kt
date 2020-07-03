package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getGameWorld
import com.almasb.fxgl.entity.component.Component
import com.github.wakingrufus.rpg.battle.BattleStateKeys
import javafx.beans.value.ChangeListener

class FieldComponent : Component() {
    private val listener = ChangeListener<Boolean> { observable, oldValue, newValue ->
        if (newValue) {
            this.entity.setUpdateEnabled(false)
        } else {
            this.entity.setUpdateEnabled(true)
        }
    }

    override fun onAdded() {
        getGameWorld().properties.booleanProperty(BattleStateKeys.inBattle).addListener(listener)
    }

    override fun onRemoved() {
        getGameWorld().properties.booleanProperty(BattleStateKeys.inBattle).removeListener(listener)
    }
}
