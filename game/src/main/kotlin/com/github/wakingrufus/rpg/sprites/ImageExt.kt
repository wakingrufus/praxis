package com.github.wakingrufus.rpg.sprites

import com.almasb.fxgl.texture.map
import javafx.scene.image.Image

fun Image.blit(image: Image): Image {
    return this.map(image) { a, b -> if (a.color.opacity == 0.0) b else a }
}
