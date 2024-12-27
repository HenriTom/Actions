package de.henritom.actions.util

import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

class MessageUtil {

    fun sendChat(message: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message))
    }
}