package com.github.wakingrufus.rpg.area

val areaData = mapOf(
        "home" to area("Home", "map.tmx"){
            spawn("Golem","Golem_02",500.0, 500.0, 10.0){
                enemy("golem A", 100, "Golem_02",10)
            }
            mapObject(350.0,500.0,40.0,40.0)
        }
)