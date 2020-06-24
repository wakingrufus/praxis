package com.github.wakingrufus.rpg.battle

import com.almasb.fxgl.dsl.getGameScene
import com.almasb.fxgl.dsl.getUIFactoryService
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.VBox
import javafx.scene.paint.Color

class BattleDialog {
    private var title: String? = null
    val choices: MutableList<Pair<String, () -> Unit>> = mutableListOf()
    fun choice(name: String, action: () -> Unit) {
        choices.add(name to action)
    }

    fun title(title: String) {
        this.title = title
    }

    fun build(): Node {
        val vbox = VBox().apply {
            translateX = 0.0
            translateY = 800.0
            minWidth = 960.0
            maxWidth = 960.0
            minHeight = 280.0
            maxHeight = 280.0
            background = Background(BackgroundFill(Color.BLACK, null, null))
        }
        title?.also {
            vbox.children.add(getUIFactoryService().newText(it))
        }
        vbox.children.addAll(choices.map { choice ->
            getUIFactoryService().newButton(choice.first).apply {
                this.minWidth = 960.0
                this.alignment = Pos.CENTER_LEFT
                setOnAction {
                    choice.second()
                    getGameScene().removeUINode(vbox)
                }
            }
        })
        return vbox
    }
}

fun battleDialog(choices: BattleDialog.() -> Unit): Node {
    return BattleDialog().apply(choices).build()
}