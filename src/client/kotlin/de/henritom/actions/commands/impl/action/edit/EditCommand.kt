package de.henritom.actions.commands.impl.action.edit

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.commands.impl.action.edit.removeauthor.RemoveAuthorCommand
import de.henritom.actions.commands.impl.action.edit.task.TaskCommand
import de.henritom.actions.commands.impl.action.edit.trigger.TriggerCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object EditCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("edit")
            .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionManager.instance.actions.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }

                .then(RemoveAuthorCommand.register())
                .then(TaskCommand.register())
                .then(TriggerCommand.register())
            )
    }
}