package de.henritom.actions.commands.impl.action.edit.task.remove

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TaskRemoveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("edit")
            .then(ClientCommandManager.argument("taskID", IntegerArgumentType.integer(1))
                .suggests { context, builder ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    ActionManager.instance.getActionByNameID(nameID)?.tasks?.forEach { task ->
                        builder.suggest(task.id)
                    }
                    builder.buildFuture()
                }
                .then(
                    ClientCommandManager.argument("taskValue", StringArgumentType.string())
                        .executes { context ->
                            val nameID = StringArgumentType.getString(context, "name/id")
                            val taskID = IntegerArgumentType.getInteger(context, "taskID")
                            val taskValue = StringArgumentType.getString(context, "taskValue")
                            val action = ActionManager.instance.getActionByNameID(nameID)

                            if (action == null) {
                                MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                return@executes Command.SINGLE_SUCCESS
                            }

                            val task = action.tasks.find { it.id == taskID }

                            if (task == null) {
                                MessageUtil().printTranslatable("actions.task.not_found", taskID.toString())
                                return@executes Command.SINGLE_SUCCESS
                            }

                            task.value = taskValue
                            MessageUtil().printTranslatable("actions.task.edited", task.type.name, task.id.toString(), taskValue)

                            Command.SINGLE_SUCCESS
                        }
                )
            )
    }
}