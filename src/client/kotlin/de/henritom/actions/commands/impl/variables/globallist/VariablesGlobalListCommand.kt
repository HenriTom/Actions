package de.henritom.actions.commands.impl.variables.globallist

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object VariablesGlobalListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("globallist")
            .executes {
                val messageUtil = MessageUtil(null)

                messageUtil.printTranslatable("actions.variables.list.global.title", ActionsClient.globalVariableStorage.update().variables.size.toString())

                for ((name, value) in ActionsClient.globalVariableStorage.update().variables)
                    messageUtil.printTranslatable("actions.variables.list.it", name, value.toString())

                Command.SINGLE_SUCCESS
            }
    }
}