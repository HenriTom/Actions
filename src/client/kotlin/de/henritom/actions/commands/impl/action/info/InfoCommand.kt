package de.henritom.actions.commands.impl.action.info

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.commands.impl.action.info.tasks.TasksCommand
import de.henritom.actions.commands.impl.action.info.triggers.TriggersCommand
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object InfoCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("info")
            .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionManager.instance.actions.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    val action = ActionManager.instance.getActionByNameID(nameID)

                    if (action != null) {
                        MessageUtil().printTranslatable("actions.action.info.title")
                        MessageUtil().printTranslatable("actions.action.info.name", action.name)
                        MessageUtil().printTranslatable("actions.action.info.id", action.id.toString())
                        MessageUtil().printTranslatable("actions.action.info.author", action.author)
                        MessageUtil().printTranslatable("actions.action.info.triggers", action.triggers.size.toString())
                        MessageUtil().printTranslatable("actions.action.info.tasks", action.tasks.size.toString())
                    } else
                        MessageUtil().printTranslatable("actions.action.not_found", nameID)

                    Command.SINGLE_SUCCESS
                }

                .then(TasksCommand.register())
                .then(TriggersCommand.register())
            )
    }
}