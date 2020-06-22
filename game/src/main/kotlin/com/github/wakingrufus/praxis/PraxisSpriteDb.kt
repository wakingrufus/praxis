package com.github.wakingrufus.praxis

import com.github.wakingrufus.rpg.sprites.*

object PraxisSpriteDb{
    val maleLight = BaseSprite("male-light")
    val man1 = BaseSprite("man1")
    val skeleton = BaseSprite("skeleton")
    val skeletonBow = RightHandSprite("bow_skeleton")
    val skeletonArrow = LeftHandSprite("arrow_skeleton")
    val blueLongHawk = HatSprite("hair_male_longhawk_blue2")

    val skeletonWithBow = characterSpriteSheet(base = skeleton, leftHandSprite = skeletonArrow, rightHandSprite = skeletonBow)
}