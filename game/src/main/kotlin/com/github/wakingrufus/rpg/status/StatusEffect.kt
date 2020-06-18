package com.github.wakingrufus.rpg.status

import com.github.wakingrufus.rpg.RpgDsl
import com.github.wakingrufus.rpg.battle.BattleComponent

class StatusEffect(val name: String,
                   val onApply : BattleComponent.() -> Unit,
                   val onRemove : BattleComponent.() -> Unit,
                   val periodic : BattleComponent.() -> Unit) {

}

@RpgDsl
class StatusEffectBuilder(val name: String){
  private  var onApply : BattleComponent.() -> Unit = {}
    private  var onRemove : BattleComponent.() -> Unit = {}
    private var periodic : BattleComponent.() -> Unit = {}
    fun periodic(effect: BattleComponent.() -> Unit){
        periodic = effect
    }
    fun build(): StatusEffect{
        return StatusEffect(name,onApply, onRemove, periodic)
    }
}