package de.henritom.actions.util

import de.henritom.actions.scheduler.ActionScheduler
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MessageUtil {

    companion object {
        val consoleLog = mutableListOf<String>()
    }

    fun printChat(message: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message))
    }

    fun printConsole(message: String, scheduler: ActionScheduler? = null) {
        if (scheduler != null)
            consoleLog.add("[${LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] (${scheduler.action.name})[${scheduler.runID}]: $message")

        println(message)
    }

    fun printTranslatable(key: String, vararg vars: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.translatable(key, *vars.map { Text.literal(it) }.toTypedArray()))
    }

    fun sendCommand(command: String) {
        MinecraftClient.getInstance().player?.networkHandler?.sendCommand(command)
    }

    fun sayChat(message: String) {
        MinecraftClient.getInstance().player?.networkHandler?.sendChatMessage(message)
    }

    fun clearConsole(): Int {
        consoleLog.clear()
        return 0
    }
}
