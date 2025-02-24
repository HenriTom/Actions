package de.henritom.actions.commands.impl.variables.remove

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object VariablesRemoveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("remove")
            .then(
                ClientCommandManager.argument("name", StringArgumentType.string())
                    .suggests { _, builder ->
                        ActionsClient.localVariableStorage.variables.keys.forEach { varName ->
                            builder.suggest(varName)
                        }
                        builder.buildFuture()
                    }
                    .executes { context ->
                        val name = StringArgumentType.getString(context, "name")
                        ActionsClient.localVariableStorage.removeVariable(name)

                        MessageUtil(null).printTranslatable("actions.variables.removed", name)

                        1
                    }
            )
    }
}