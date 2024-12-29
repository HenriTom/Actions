package de.henritom.actions.motion

import net.minecraft.client.MinecraftClient

class MoveManager {

    fun setMovement(move: MoveEnum): MoveManager {
        val options = MinecraftClient.getInstance().options

        when (move) {
            MoveEnum.FORWARD -> options.forwardKey.isPressed = true
            MoveEnum.BACKWARD -> options.forwardKey.isPressed = true
            MoveEnum.LEFT -> options.leftKey.isPressed = true
            MoveEnum.RIGHT -> options.rightKey.isPressed = true
            MoveEnum.JUMP -> options.jumpKey.isPressed = true
            MoveEnum.SNEAK -> options.sneakKey.isPressed = true
            MoveEnum.SPRINT -> options.sprintKey.isPressed = true
            MoveEnum.STOP -> {
                options.forwardKey.isPressed = false
                options.leftKey.isPressed = false
                options.rightKey.isPressed = false
                options.jumpKey.isPressed = false
                options.sneakKey.isPressed = false
                options.sprintKey.isPressed = false
            }
        }

        return this
    }
}