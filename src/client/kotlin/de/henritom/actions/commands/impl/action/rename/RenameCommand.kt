package de.henritom.actions.commands.impl.action.rename

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object RenameCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("rename")
            .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionManager.instance.actions.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }

                .then(ClientCommandManager.argument("newName", StringArgumentType.string())
                    .executes { context ->
                        val name = StringArgumentType.getString(context, "name/id")
                        val newName = StringArgumentType.getString(context, "newName")

                        val action = ActionManager.instance.getActionByNameID(name)

                        if (action?.file == null) {
                            MessageUtil().printTranslatable("actions.action.not_found", name)
                            return@executes Command.SINGLE_SUCCESS
                        }

                        ActionEditManager.instance.renameAction(action, newName)

                        MessageUtil().printTranslatable("actions.action.renamed", name, newName)

                        Command.SINGLE_SUCCESS
                    }
                )
            )
    }
}