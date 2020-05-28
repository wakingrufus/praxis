package com.github.wakingrufus.rpg

import com.almasb.fxgl.entity.Entity
import com.almasb.fxgl.entity.component.Component

/**
 * committed upstream, will be in future release
 */
inline fun  <reified T : Component> Entity.getComponent(): T{
  return this.getComponent(T::class.java)
}
