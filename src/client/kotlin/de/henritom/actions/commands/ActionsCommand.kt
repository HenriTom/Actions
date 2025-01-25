package de.henritom.actions.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.motion.MoveEnum
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.triggers.settings.ReceiveMessageEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader

class ActionsCommand {
    companion object {
        fun register() {
            ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
                dispatcher.register(
                    ClientCommandManager.literal("actions")
                        .then(ClientCommandManager.literal("action")
                            .then(ClientCommandManager.literal("call")
                                .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ActionManager.instance.actions.filter { action ->
                                            for (trigger in action.triggers)
                                                if (trigger.type == TriggerEnum.CALL)
                                                    return@filter true
                                            false
                                        }.forEach { action ->
                                            builder.suggest(action.name)
                                        }
                                        builder.buildFuture()
                                    }
                                    .executes { context ->
                                        val nameID = StringArgumentType.getString(context, "name/id")

                                        val actionName = ActionManager.instance.getActionByNameID(nameID)?.name ?: nameID

                                        when (ActionManager.instance.callAction(nameID)) {
                                            1 -> MessageUtil().printTranslatable("actions.action.called", actionName)
                                            2 -> MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                            3 -> MessageUtil().printTranslatable("actions.action.not_callable", actionName)
                                        }

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(ClientCommandManager.literal("create")
                                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                    .executes { context ->
                                        val name = StringArgumentType.getString(context, "name")

                                        when (ActionManager.instance.createAction(name)) {
                                            1 -> MessageUtil().printTranslatable("actions.action.created", name)
                                            2 -> MessageUtil().printTranslatable("actions.action.already_used", name)
                                            3 -> MessageUtil().printTranslatable("actions.action.start_with_letter")
                                            4 -> MessageUtil().printTranslatable("actions.action.min_length")
                                        }

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(ClientCommandManager.literal("delete")
                                .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ActionManager.instance.actions.forEach { action ->
                                            builder.suggest(action.name)
                                        }
                                        builder.buildFuture()
                                    }
                                    .executes { context ->
                                        val nameID = StringArgumentType.getString(context, "name/id")

                                        val actionName = ActionManager.instance.getActionByNameID(nameID)?.name ?: nameID

                                        if (ActionManager.instance.deleteAction(nameID))
                                            MessageUtil().printTranslatable("actions.action.deleted", actionName)

                                        else
                                            MessageUtil().printTranslatable("actions.action.not_found", actionName)

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(ClientCommandManager.literal("disable")
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

                                        if (action == null) {
                                            MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                            return@executes Command.SINGLE_SUCCESS
                                        }

                                        if (ActionEditManager.instance.disableAction(action)) {
                                            MessageUtil().printTranslatable("actions.action.disabled", action.name)
                                            ConfigManager().reloadActions()
                                            MessageUtil().printTranslatable("actions.file.reloaded.actions")
                                        } else
                                            MessageUtil().printTranslatable("actions.action.not_disabled", action.name)

                                        Command.SINGLE_SUCCESS
                                    }
                                ))

                            .then(ClientCommandManager.literal("enable")
                                .then(ClientCommandManager.argument("name", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ConfigManager().getDisabledActions().forEach { file ->
                                            builder.suggest(file.nameWithoutExtension)
                                        }
                                        builder.buildFuture()
                                    }
                                    .executes { context ->
                                        val name = StringArgumentType.getString(context, "name")

                                        val file = FabricLoader.getInstance().configDir.toFile().resolve("actions/actions/$name.disabled")

                                        if (ActionEditManager.instance.enableAction(file)) {
                                            MessageUtil().printTranslatable("actions.action.enabled", name)
                                            ConfigManager().reloadActions()
                                            MessageUtil().printTranslatable("actions.file.reloaded.actions")
                                        } else
                                            MessageUtil().printTranslatable("actions.action.not_enabled", name)

                                        Command.SINGLE_SUCCESS
                                    }
                                ))

                            .then(ClientCommandManager.literal("edit")
                                .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ActionManager.instance.actions.forEach { action ->
                                            builder.suggest(action.name)
                                        }
                                        builder.buildFuture()
                                    }
                                    .then(ClientCommandManager.literal("trigger")
                                        .then(ClientCommandManager.literal("add")
                                            .then(ClientCommandManager.argument("trigger", StringArgumentType.string())
                                                .suggests { context, builder ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    val action = ActionManager.instance.getActionByNameID(nameID)

                                                    for (trigger in TriggerEnum.entries)
                                                        if (trigger != TriggerEnum.CALL || action?.triggers?.none { it.type == TriggerEnum.CALL } == true)
                                                            builder.suggest(trigger.name)

                                                    builder.buildFuture()
                                                }
                                                .then(ClientCommandManager.argument("initValue", StringArgumentType.string())
                                                    .suggests { context, builder ->
                                                        val trigger = try {
                                                            TriggerEnum.valueOf(StringArgumentType.getString(context, "trigger"))
                                                        } catch (e: IllegalArgumentException) {
                                                            builder.buildFuture()
                                                        }

                                                        if (trigger == TriggerEnum.RECEIVE_MESSAGE)
                                                            for (receiveType in ReceiveMessageEnum.entries)
                                                                if (receiveType != ReceiveMessageEnum.ANY)
                                                                    builder.suggest("${receiveType.name}-")
                                                                else
                                                                    builder.suggest(receiveType.name)

                                                        builder.buildFuture()
                                                    }
                                                    .executes { context ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        val triggerName = StringArgumentType.getString(context, "trigger")
                                                        val initValue = StringArgumentType.getString(context, "initValue")
                                                        val action = ActionManager.instance.getActionByNameID(nameID)

                                                        if (action == null) {
                                                            MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        val trigger = try {
                                                            TriggerEnum.valueOf(triggerName)
                                                        } catch (e: IllegalArgumentException) {
                                                            MessageUtil().printTranslatable("actions.trigger.not_found", nameID)
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        if (ActionEditManager.instance.addTrigger(action, trigger) == 1) {
                                                            action.triggers.last().value = initValue

                                                            MessageUtil().printTranslatable("actions.trigger.added.initial_value", trigger.name, nameID, initValue)
                                                        } else if (ActionEditManager.instance.addTrigger(action, trigger) == 2)
                                                            MessageUtil().printTranslatable("actions.trigger.multiple_calls")

                                                        Command.SINGLE_SUCCESS
                                                    })
                                                .executes { context ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    val triggerName = StringArgumentType.getString(context, "trigger")
                                                    val action = ActionManager.instance.getActionByNameID(nameID)

                                                    if (action == null) {
                                                        MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    val trigger = try {
                                                        TriggerEnum.valueOf(triggerName)
                                                    } catch (e: IllegalArgumentException) {
                                                        MessageUtil().printTranslatable("actions.trigger.not_found", triggerName)
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    if (ActionEditManager.instance.addTrigger(action, trigger) == 1)
                                                        MessageUtil().printTranslatable("actions.trigger.added", trigger.name, nameID)
                                                    else if (ActionEditManager.instance.addTrigger(action, trigger) == 2)
                                                        MessageUtil().printTranslatable("actions.trigger.multiple_calls")

                                                    Command.SINGLE_SUCCESS
                                                }))

                                        .then(ClientCommandManager.literal("remove")
                                            .then(ClientCommandManager.argument("triggerID", IntegerArgumentType.integer(1))
                                                .suggests { context, builder ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    ActionManager.instance.getActionByNameID(nameID)?.triggers?.forEach { trigger ->
                                                        builder.suggest(trigger.id)
                                                    }
                                                    builder.buildFuture()
                                                }.executes { context ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    val triggerID = IntegerArgumentType.getInteger(context, "triggerID")
                                                    val action = ActionManager.instance.getActionByNameID(nameID)

                                                    if (action == null) {
                                                        MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    val trigger = action.triggers.find { it.id == triggerID }

                                                    if (trigger == null) {
                                                        MessageUtil().printTranslatable("actions.trigger.not_found", triggerID.toString())
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    if (ActionEditManager.instance.removeTrigger(action, trigger))
                                                        MessageUtil().printTranslatable("actions.trigger.removed", trigger.type.name, trigger.id.toString(), action.name)
                                                    else
                                                        MessageUtil().printTranslatable("actions.trigger.not_found_in_action", trigger.type.name, action.name)

                                                    Command.SINGLE_SUCCESS
                                                }))

                                        .then(ClientCommandManager.literal("edit")
                                            .then(ClientCommandManager.argument("triggerID", IntegerArgumentType.integer(1))
                                                .suggests { context, builder ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    ActionManager.instance.getActionByNameID(nameID)?.triggers?.forEach { trigger ->
                                                        if (trigger.type != TriggerEnum.CALL)
                                                            builder.suggest(trigger.id)
                                                    }
                                                    builder.buildFuture()
                                                }
                                                .then(ClientCommandManager.argument("triggerValue", StringArgumentType.string())
                                                    .executes { context ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        val triggerID = IntegerArgumentType.getInteger(context, "triggerID")
                                                        val triggerValue = StringArgumentType.getString(context, "triggerValue")
                                                        val action = ActionManager.instance.getActionByNameID(nameID)

                                                        if (action == null) {
                                                            MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        val trigger = action.triggers.find { it.id == triggerID }

                                                        if (trigger == null) {
                                                            MessageUtil().printTranslatable("actions.trigger.not_found", triggerID.toString())
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        if (trigger.type == TriggerEnum.CALL) {
                                                            MessageUtil().printTranslatable("actions.trigger.not_editable")
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        trigger.value = triggerValue
                                                        MessageUtil().printTranslatable("actions.trigger.edited", trigger.type.name, trigger.id.toString(), triggerValue)

                                                        Command.SINGLE_SUCCESS
                                                    }))))

                                        .then(ClientCommandManager.literal("task")
                                            .then(ClientCommandManager.literal("add")
                                                .then(ClientCommandManager.argument("task", StringArgumentType.string())
                                                    .suggests { _, builder ->
                                                        for (task in TaskEnum.entries)
                                                            builder.suggest(task.name)

                                                        builder.buildFuture()
                                                    }
                                                    .then(ClientCommandManager.argument("initValue", StringArgumentType.string())
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
                                                    }))

                                            .then(ClientCommandManager.literal("remove")
                                                .then(ClientCommandManager.argument("taskID", IntegerArgumentType.integer(1))
                                                    .suggests { context, builder ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        ActionManager.instance.getActionByNameID(nameID)?.tasks?.forEach { task ->
                                                            builder.suggest(task.id)
                                                        }
                                                        builder.buildFuture()
                                                    }.executes { context ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        val taskID = IntegerArgumentType.getInteger(context, "taskID")
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

                                                        if (ActionEditManager.instance.removeTask(action, task))
                                                            MessageUtil().printTranslatable("actions.task.removed", task.type.name, task.id.toString(), action.name)
                                                        else
                                                            MessageUtil().printTranslatable("actions.task.not_found_in_action", task.type.name, action.name)

                                                        Command.SINGLE_SUCCESS
                                                    }))

                                            .then(ClientCommandManager.literal("edit")
                                                .then(ClientCommandManager.argument("taskID", IntegerArgumentType.integer(1))
                                                    .suggests { context, builder ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        ActionManager.instance.getActionByNameID(nameID)?.tasks?.forEach { task ->
                                                            builder.suggest(task.id)
                                                        }
                                                        builder.buildFuture()
                                                    }
                                                    .then(ClientCommandManager.argument("taskValue", StringArgumentType.string())
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
                                                        }))))

                                    .then(ClientCommandManager.literal("removeauthor")
                                        .executes { context ->
                                            val nameID = StringArgumentType.getString(context, "name/id")
                                            val action = ActionManager.instance.getActionByNameID(nameID)

                                            if (action == null) {
                                                MessageUtil().printTranslatable("actions.action.not_found", nameID)
                                                return@executes Command.SINGLE_SUCCESS
                                            }

                                            if (ActionEditManager.instance.removeAuthor(action))
                                                MessageUtil().printTranslatable("actions.author.removed", action.name)
                                            else
                                                MessageUtil().printTranslatable("actions.author.not_found", action.name)

                                            Command.SINGLE_SUCCESS
                                        })))

                            .then(ClientCommandManager.literal("info")
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

                                    .then(ClientCommandManager.literal("triggers")
                                        .executes { context ->
                                            val nameID = StringArgumentType.getString(context, "name/id")
                                            val action = ActionManager.instance.getActionByNameID(nameID)

                                            if (action != null) {
                                                MessageUtil().printTranslatable("actions.action.triggers.title", action.triggers.size.toString())

                                                for (trigger in action.triggers)
                                                    if (trigger.type == TriggerEnum.CALL)
                                                        MessageUtil().printTranslatable("actions.action.triggers.it.call", trigger.type.name, trigger.id.toString())
                                                    else
                                                        MessageUtil().printTranslatable("actions.action.triggers.it.other", trigger.type.name, trigger.value.toString(), trigger.id.toString())
                                            }

                                            Command.SINGLE_SUCCESS
                                        }
                                    )

                                    .then(ClientCommandManager.literal("tasks")
                                        .executes { context ->
                                            val nameID = StringArgumentType.getString(context, "name/id")
                                            val action = ActionManager.instance.getActionByNameID(nameID)

                                            if (action != null) {
                                                MessageUtil().printTranslatable("actions.action.tasks.title", action.tasks.size.toString())
                                                MessageUtil().printTranslatable("actions.action.tasks.it.first")

                                                for (task in action.tasks)
                                                    if (task.type == TaskEnum.COMMENT)
                                                        MessageUtil().printTranslatable("actions.action.tasks.it.comment", task.value.toString(), task.id.toString())
                                                    else
                                                        MessageUtil().printTranslatable("actions.action.tasks.it.other", task.type.name, task.value.toString(), task.id.toString())

                                                MessageUtil().printTranslatable("actions.action.tasks.it.last")
                                            }

                                            Command.SINGLE_SUCCESS
                                        }
                                    )
                                )))

                        .then(ClientCommandManager.literal("list")
                            .executes {
                                MessageUtil().printTranslatable("actions.list.title", ActionManager.instance.actions.size.toString())

                                for (action in ActionManager.instance.actions)
                                    MessageUtil().printTranslatable("actions.list.it", action.name, action.id.toString())

                                Command.SINGLE_SUCCESS
                            })

                        .then(ClientCommandManager.literal("version")
                            .executes {
                                MessageUtil().printTranslatable("actions.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString())

                                Command.SINGLE_SUCCESS
                            })

                        .then(ClientCommandManager.literal("file")
                            .then(ClientCommandManager.literal("reload")
                                .then(ClientCommandManager.literal("config")
                                    .executes {
                                        ConfigManager().loadConfig()
                                        MessageUtil().printTranslatable("actions.file.reloaded.config")
                                        Command.SINGLE_SUCCESS
                                    })

                                .then(ClientCommandManager.literal("actions")
                                    .executes {
                                        ConfigManager().reloadActions()
                                        MessageUtil().printTranslatable("actions.file.reloaded.actions")
                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(ClientCommandManager.literal("save")
                                .then(ClientCommandManager.literal("actions")
                                    .executes {
                                        ConfigManager().saveAllActions()
                                        MessageUtil().printTranslatable("actions.file.save.actions")
                                        Command.SINGLE_SUCCESS
                                    })

                                .then(ClientCommandManager.literal("config")
                                    .executes {
                                        ConfigManager().saveConfig()
                                        MessageUtil().printTranslatable("actions.file.save.config")
                                        Command.SINGLE_SUCCESS
                                    })))

                            .then(ClientCommandManager.literal("prefix")
                                .then(ClientCommandManager.literal("set")
                                    .then(ClientCommandManager.argument("prefix", StringArgumentType.string())
                                        .executes { context ->
                                            val prefix = StringArgumentType.getString(context, "prefix")
                                            ActionManager.instance.commandPrefix = prefix
                                            MessageUtil().printTranslatable("actions.prefix.set", prefix)
                                            Command.SINGLE_SUCCESS
                                        }))

                                .then(ClientCommandManager.literal("clear")
                                    .executes {
                                        ActionManager.instance.commandPrefix = ""
                                        MessageUtil().printTranslatable("actions.prefix.clear")
                                        Command.SINGLE_SUCCESS
                                    })
                        )
                )
            }
        }
    }
}