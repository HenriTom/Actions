package de.henritom.actions.commands.impl.variables.add

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object VariablesAddCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("add")
            .then(
                ClientCommandManager.argument("name", StringArgumentType.string())
                    .then(
                        ClientCommandManager.argument("value", StringArgumentType.string())
                            .executes { context ->
                                val name = StringArgumentType.getString(context, "name")
                                val value = StringArgumentType.getString(context, "value")
                                ActionsClient.localVariableStorage.addVariable(name, value)

                                MessageUtil(null).printTranslatable("actions.variables.added", name, value)

                                1
                            }
                    )
            )
    }
}