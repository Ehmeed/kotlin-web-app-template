package com.ehmeed.js

enum class KeyCode(val code: Int) {
    UP(38),
    DOWN(40),
    LEFT(37),
    RIGHT(39),
    Q(81),
    IGNORED(-1)
}

fun Int.asKeyCode() = when(this) {
    38 -> KeyCode.UP
    40 -> KeyCode.DOWN
    39 -> KeyCode.RIGHT
    37 -> KeyCode.LEFT
    81 -> KeyCode.Q
    else -> KeyCode.IGNORED
}
