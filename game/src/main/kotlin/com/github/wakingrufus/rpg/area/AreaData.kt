package com.github.wakingrufus.rpg.area

val areaData = mapOf(
        "home" to area("Home", "map.tmx"){
            spawn("Golem","Golem_01/Idle/Golem_01_Idle_000.png",500.0, 500.0, 10.0){
                enemy("golem A", 100, "Golem_01/Idle/Golem_01_Idle_000.png",10)
            }
            mapObject(350.0,500.0,40.0,40.0)
        }
)