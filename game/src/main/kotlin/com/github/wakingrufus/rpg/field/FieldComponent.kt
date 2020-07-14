package com.github.wakingrufus.rpg.field

import com.almasb.fxgl.dsl.getbp
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
        getbp(BattleStateKeys.inBattle).addListener(listener)
    }

    override fun onRemoved() {
        getbp(BattleStateKeys.inBattle).removeListener(listener)
    }
}
