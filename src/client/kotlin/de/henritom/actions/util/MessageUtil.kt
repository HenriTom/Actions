package de.henritom.actions.util

import de.henritom.actions.ActionsClient
import de.henritom.actions.actions.Action
import de.henritom.actions.scheduler.ActionScheduler
import net.minecraft.client.MinecraftClient
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MessageUtil(var action: Action?) {

    companion object {
        val consoleLog = mutableListOf<String>()
    }

    fun printChat(message: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message))
    }

    fun printConsole(message: String, scheduler: ActionScheduler? = null) {
        if (scheduler != null)
            consoleLog.add("[${LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))}] (${scheduler.action.name})[${scheduler.runID}]: $message")
    }

    fun printTranslatable(key: String, vararg vars: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.translatable(key, *vars.map { Text.literal(it) }.toTypedArray()))
    }

    fun printTranslatableClickable(key: String, urlString: String, vararg vars: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.translatable(key, *vars.map { Text.literal(it) }.toTypedArray()).styled { style -> style.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, urlString)) })
    }

    fun printTranslatableClickable(message: String, urlString: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.translatable(message).styled { style -> style.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, urlString)) })
    }

    fun printClickable(message: String, urlString: String) {
        MinecraftClient.getInstance().inGameHud.chatHud.addMessage(Text.literal(message).styled { style -> style.withClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, urlString)) })
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

    fun sendCommandByAction(command: String) {
        sendCommand(translateVariables(command))
    }

    fun sayChatByAction(message: String) {
        sayChat(translateVariables(message))
    }

    fun printChatByAction(string: String) {
        printChat(translateVariables(string))
    }

    fun printConsoleByAction(message: String, scheduler: ActionScheduler? = null) {
        printConsole(translateVariables(message), scheduler)
    }

    fun translateVariables(message: String): String {
        var result = message

        if (message.contains("%gvar.")) {
            val regex = Regex("""%gvar\.(\w+)""")

            regex.findAll(message).forEach { matchResult ->
                val variableName = matchResult.groupValues[1]
                result = result.replace("%gvar.$variableName", ActionsClient.globalVariableStorage.update().getVariable(variableName).toString())
            }
        }

        if (message.contains("%cvar.") && action != null) {
            val regex = Regex("""%cvar\.(\w+)""")

            regex.findAll(message).forEach { matchResult ->
                val index = matchResult.groupValues[1]
                val numberIndex = index.toIntOrNull()

                result = if (numberIndex == null || index == "all")
                    result.replace("%cvar.$index", action!!.callArgs.toString())
                else
                    if (action!!.callArgs.size > numberIndex) {
                        result.replace("%cvar.$index", action!!.callArgs[numberIndex].toString())
                    } else
                        result.replace("%cvar.$index", "null")
            }
        }

        if (message.contains("%lvar.")) {
            val regex = Regex("""%lvar\.(\w+)""")

            regex.findAll(message).forEach { matchResult ->
                val variableName = matchResult.groupValues[1]
                result = result.replace("%lvar.$variableName", ActionsClient.localVariableStorage.getVariable(variableName).toString())
            }
        }

        return result
    }
}
