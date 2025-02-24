package de.henritom.actions.commands.impl.variables.list

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.region.RegionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object VariablesListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("list")
            .executes {
                val messageUtil = MessageUtil(null)

                messageUtil.printTranslatable("actions.variables.list.local.title", ActionsClient.localVariableStorage.variables.size.toString())

                for ((name, value) in ActionsClient.localVariableStorage.variables)
                    messageUtil.printTranslatable("actions.variables.list.it", name, value.toString())

                Command.SINGLE_SUCCESS
            }
    }
}