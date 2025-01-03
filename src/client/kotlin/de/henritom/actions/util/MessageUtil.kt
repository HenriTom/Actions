package de.henritom.actions.util

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class MessageUtil {

    fun printChat(message: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message))
    }

    fun sendCommand(command: String) {
        MinecraftClient.getInstance().player?.networkHandler?.sendCommand(command)
    }

    fun sayChat(message: String) {
        MinecraftClient.getInstance().player?.networkHandler?.sendChatMessage(message)
    }
}
