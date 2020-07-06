package com.github.wakingrufus.rpg.sprites

import com.almasb.fxgl.dsl.FXGL
import com.almasb.fxgl.texture.AnimationChannel
import com.almasb.fxgl.texture.getDummyImage
import javafx.scene.image.Image
import javafx.util.Duration

enum class AnimationType(val path: String) {
    ATTACKING("Attacking"),
    IDLE("Idle"),
    WALKING("Walking")
}

fun getSprite(name: String, type: AnimationType, frame: Int): String {
    return "$name/${type.path}/${name}_${type.path}_${frame.toString().padStart(3, '0')}.png"
}

fun getAnimation(name: String, type: AnimationType, duration: Duration): AnimationChannel {
    val list: MutableList<Image> = mutableListOf()
    var index = 0
    var nextImage = FXGL.image(getSprite(name, type, index))
    while (nextImage != getDummyImage()) {
        list.add(nextImage)
        index++
        nextImage = FXGL.image(getSprite(name, type, index))
    }
    return AnimationChannel(list, duration)
}

enum class AttackAnimationType {
    SPELL, THRUST, SLASH, SHOOT
}

enum class SpriteOrientation {
    NORTH, WEST, SOUTH, EAST
}

class LPCSpriteSheet(val image: Image) {
    val spellCastNorth = of(0, 7)
    val spellCastWest = of(1, 7)
    val spellCastSouth = of(2, 7)
    val spellCastEast = of(3, 7)
    val thrustNorth = of(4, 8)
    val thrustWest = of(5, 8)
    val thrustSouth = of(6, 8)
    val thrustEast = of(7, 8)
    val walkNorth = of(8, 9)
    val idleNorth = of(8, 1)
    val walkWest = of(9, 9)
    val idleWest = of(9, 1)
    val walkSouth = of(10, 9)
    val idleSouth = of(10, 1)
    val walkEast = of(11, 9)
    val idleEast = of(11, 1)
    val slashNorth = of(12, 6)
    val slashWest = of(13, 6)
    val slashSouth = of(14, 6)
    val slashEast = of(15, 6)
    val shootNorth = of(16, 13)
    val shootWest = of(17, 13)
    val shootSouth = of(18, 13)
    val shootEast = of(19, 13)
    val die = of(20, 6)
    private fun of(row: Int, frames: Int): AnimationChannel {
        return AnimationChannel(image, 13, 64, 64, Duration.seconds(1.0), 13 * row, (13 * row) + frames - 1)
    }

    fun getIdleAnimation(orientation: SpriteOrientation): AnimationChannel {
        return when (orientation) {
            SpriteOrientation.NORTH -> idleNorth
            SpriteOrientation.WEST -> idleWest
            SpriteOrientation.SOUTH -> idleSouth
            SpriteOrientation.EAST -> idleEast
        }
    }

    fun getAttackAnimation(type: AttackAnimationType, orientation: SpriteOrientation): AnimationChannel {
        return when (type) {
            AttackAnimationType.SPELL -> when (orientation) {
                SpriteOrientation.NORTH -> spellCastNorth
                SpriteOrientation.WEST -> spellCastWest
                SpriteOrientation.SOUTH -> spellCastSouth
                SpriteOrientation.EAST -> spellCastEast
            }
            AttackAnimationType.THRUST -> when (orientation) {
                SpriteOrientation.NORTH -> thrustNorth
                SpriteOrientation.WEST -> thrustWest
                SpriteOrientation.SOUTH -> thrustSouth
                SpriteOrientation.EAST -> thrustEast
            }
            AttackAnimationType.SLASH -> when (orientation) {
                SpriteOrientation.NORTH -> slashNorth
                SpriteOrientation.WEST -> slashWest
                SpriteOrientation.SOUTH -> slashSouth
                SpriteOrientation.EAST -> slashEast
            }
            AttackAnimationType.SHOOT -> when (orientation) {
                SpriteOrientation.NORTH -> shootNorth
                SpriteOrientation.WEST -> shootWest
                SpriteOrientation.SOUTH -> shootSouth
                SpriteOrientation.EAST -> shootEast
            }
        }
    }
}

sealed class Sprite(val name: String)
class BaseSprite(name: String) : Sprite(name)
class HatSprite(name: String) : Sprite(name)
class ArmorSprite(name: String) : Sprite(name)
class ShoesSprite(name: String) : Sprite(name)
class LeftHandSprite(name: String) : Sprite(name)
class RightHandSprite(name: String) : Sprite(name)

fun blittedSpriteSheet(names: List<String>): LPCSpriteSheet {
    return LPCSpriteSheet(names.fold(null as Image?) { r, t ->
        r?.blit(FXGL.image("$t.png")) ?: FXGL.image("$t.png")
    } ?: getDummyImage())
}

fun characterSpriteSheet(base: BaseSprite,
                         armorSprite: ArmorSprite? = null,
                         shoesSprite: ShoesSprite? = null,
                         leftHandSprite: LeftHandSprite? = null,
                         rightHandSprite: RightHandSprite? = null,
                         hat: HatSprite? = null): LPCSpriteSheet {
    return blittedSpriteSheet(listOfNotNull(hat, leftHandSprite, rightHandSprite, armorSprite, shoesSprite, base)
            .map(Sprite::name))
}

fun singleSpriteSheet(name: String): LPCSpriteSheet {
    return LPCSpriteSheet(FXGL.image("$name.png")
    )
}
