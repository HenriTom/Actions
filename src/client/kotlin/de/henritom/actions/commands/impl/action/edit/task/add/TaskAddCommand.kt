package de.henritom.actions.commands.impl.action.edit.task.add

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.motion.MoveEnum
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TaskAddCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("add")
            .then(ClientCommandManager.argument("task", StringArgumentType.string())
                .suggests { _, builder ->
                    for (task in TaskEnum.entries)
                        builder.suggest(task.name)

                    builder.buildFuture()
                }
                .then(
                    ClientCommandManager.argument("initValue", StringArgumentType.string())
                        .suggests { context, builder ->
                            val task = try {
                                TaskEnum.valueOf(StringArgumentType.getString(context, "task"))
                            } catch (e: IllegalArgumentException) {
                                builder.buildFuture()
                            }

                            if (task == TaskEnum.MOVE)
                                for (move in MoveEnum.entries)
                                    builder.suggest(move.name)

                            if (task == TaskEnum.MINE || task == TaskEnum.USE)
                                builder.suggest("true").suggest("false")

                            builder.buildFuture()
                        }
                        .executes { context ->
                            val nameID = StringArgumentType.getString(context, "name/id")
                            val taskName = StringArgumentType.getString(context, "task")
                            val initValue = StringArgumentType.getString(context, "initValue")
                            val action = ActionManager.instance.getActionByNameID(nameID)

                            if (action == null) {
                                MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                return@executes Command.SINGLE_SUCCESS
                            }

                            val task = try {
                                TaskEnum.valueOf(taskName)
                            } catch (e: IllegalArgumentException) {
                                MessageUtil().printTranslatable("actions.task.not_found", taskName)
                                return@executes Command.SINGLE_SUCCESS
                            }

                            if (ActionEditManager.instance.addTask(action, task)) {
                                action.tasks.last().value = initValue

                                MessageUtil().printTranslatable("actions.task.added.initial_value", task.name, nameID, initValue)
                            }

                            Command.SINGLE_SUCCESS
                        })
                .executes { context ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    val taskName = StringArgumentType.getString(context, "task")
                    val action = ActionManager.instance.getActionByNameID(nameID)

                    if (action == null) {
                        MessageUtil().printTranslatable("actions.action.not_found", nameID)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    val task = try {
                        TaskEnum.valueOf(taskName)
                    } catch (e: IllegalArgumentException) {
                        MessageUtil().printTranslatable("actions.task.not_found", taskName)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    if (ActionEditManager.instance.addTask(action, task))
                        MessageUtil().printTranslatable("actions.task.added", task.name, nameID)

                    Command.SINGLE_SUCCESS
                }
            )
    }
}