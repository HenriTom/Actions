package de.henritom.actions.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.trigger.Trigger
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.text.Text

class ActionsCommand {
    companion object {
        fun register() {
            CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
                dispatcher.register(
                    CommandManager.literal("actions")
                        .then(CommandManager.literal("action")
                            .then(CommandManager.literal("call")
                                .then(CommandManager.argument("name/id", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ActionManager.instance.actions.filter { it.triggers.contains(Trigger.CALL) }.forEach { action ->
                                            builder.suggest(action.name)
                                        }
                                        builder.buildFuture()
                                    }
                                    .executes { context ->
                                        val nameID = StringArgumentType.getString(context, "name/id")

                                        when (ActionManager.instance.callAction(nameID)) {
                                            1 -> context.source.sendMessage(Text.literal("§8» §7Action $nameID called."))
                                            2 -> context.source.sendMessage(Text.literal("§8» §7Action $nameID not found."))
                                            3 -> context.source.sendMessage(Text.literal("§8» §7Action $nameID is not callable."))
                                        }

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(CommandManager.literal("create")
                                .then(CommandManager.argument("name", StringArgumentType.string())
                                    .executes { context ->
                                        val name = StringArgumentType.getString(context, "name")

                                        when (ActionManager.instance.createAction(name)) {
                                            1 -> context.source.sendMessage(Text.literal("§8» §7Action $name created."))
                                            2 -> context.source.sendMessage(Text.literal("§8» §7Name $name already used."))
                                            3 -> context.source.sendMessage(Text.literal("§8» §7Name must start with a letter."))
                                            4 -> context.source.sendMessage(Text.literal("§8» §7Name must be at least 3 characters long."))
                                        }

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(CommandManager.literal("delete")
                                .then(CommandManager.argument("name/id", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ActionManager.instance.actions.forEach { action ->
                                            builder.suggest(action.name)
                                        }
                                        builder.buildFuture()
                                    }
                                    .executes { context ->
                                        val nameID = StringArgumentType.getString(context, "name/id")

                                        if (ActionManager.instance.deleteAction(nameID))
                                            context.source.sendMessage(Text.literal("§8» §7Action $nameID deleted."))

                                        else
                                            context.source.sendMessage(Text.literal("§8» §7Action $nameID not found."))

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(CommandManager.literal("edit")
                                .then(CommandManager.argument("name/id", StringArgumentType.string())
                                    .suggests { _, builder ->
                                        ActionManager.instance.actions.forEach { action ->
                                            builder.suggest(action.name)
                                        }
                                        builder.buildFuture()
                                    }
                                    .then(CommandManager.literal("addtrigger")
                                        .then(CommandManager.argument("trigger", StringArgumentType.string())
                                            .suggests { context, builder ->
                                                val nameID = StringArgumentType.getString(context, "name/id")
                                                val action = ActionManager.instance.getActionByNameID(nameID)
                                                if (action != null) {
                                                    Trigger.entries.filterNot { it in action.triggers }.forEach { trigger ->
                                                        builder.suggest(trigger.name)
                                                    }
                                                }
                                                builder.buildFuture()
                                            }
                                            .executes { context ->
                                                val nameID = StringArgumentType.getString(context, "name/id")
                                                val triggerName = StringArgumentType.getString(context, "trigger")
                                                val action = ActionManager.instance.getActionByNameID(nameID)

                                                if (action == null) {
                                                    context.source.sendMessage(Text.literal("§8» §7Action $nameID not found."))
                                                    return@executes Command.SINGLE_SUCCESS
                                                }

                                                val trigger = try {
                                                    Trigger.valueOf(triggerName)
                                                } catch (e: IllegalArgumentException) {
                                                    context.source.sendMessage(Text.literal("§8» §7Trigger $triggerName not found."))
                                                    return@executes Command.SINGLE_SUCCESS
                                                }

                                                if (ActionEditManager.instance.addTrigger(action, trigger))
                                                    context.source.sendMessage(Text.literal("§8» §7Trigger $triggerName added to action $nameID."))
                                                else
                                                    context.source.sendMessage(Text.literal("§8» §7Trigger $triggerName already added to action $nameID."))

                                                Command.SINGLE_SUCCESS
                                            }))

                                    .then(CommandManager.literal("removetrigger")
                                        .then(CommandManager.argument("trigger", StringArgumentType.string())
                                            .suggests { context, builder ->
                                                val nameID = StringArgumentType.getString(context, "name/id")
                                                ActionManager.instance.getActionByNameID(nameID)?.triggers?.forEach { trigger ->
                                                    builder.suggest(trigger.name)
                                                }
                                                builder.buildFuture()
                                            }.executes { context ->
                                                val nameID = StringArgumentType.getString(context, "name/id")
                                                val triggerName = StringArgumentType.getString(context, "trigger")
                                                val action = ActionManager.instance.getActionByNameID(nameID)

                                                if (action == null) {
                                                    context.source.sendMessage(Text.literal("§8» §7Action $nameID not found."))
                                                    return@executes Command.SINGLE_SUCCESS
                                                }

                                                val trigger = try {
                                                    Trigger.valueOf(triggerName)
                                                } catch (e: IllegalArgumentException) {
                                                    context.source.sendMessage(Text.literal("§8» §7Trigger $triggerName not found."))
                                                    return@executes Command.SINGLE_SUCCESS
                                                }

                                                if (ActionEditManager.instance.removeTrigger(action, trigger))
                                                    context.source.sendMessage(Text.literal("§8» §7Trigger $triggerName removed from action $nameID."))
                                                else
                                                    context.source.sendMessage(Text.literal("§8» §7Trigger $triggerName not found in action $nameID."))

                                                Command.SINGLE_SUCCESS
                                            }))

                                    .then(CommandManager.literal("removeauthor")
                                        .executes { context ->
                                            val nameID = StringArgumentType.getString(context, "name/id")
                                            val action = ActionManager.instance.getActionByNameID(nameID)

                                            if (action == null) {
                                                context.source.sendMessage(Text.literal("§8» §7Action $nameID not found."))
                                                return@executes Command.SINGLE_SUCCESS
                                            }

                                            if (ActionEditManager.instance.removeAuthor(action))
                                                context.source.sendMessage(Text.literal("§8» §7Author removed from action $nameID."))
                                            else
                                                context.source.sendMessage(Text.literal("§8» §7Author not found in action $nameID."))

                                            Command.SINGLE_SUCCESS
                                        })
                                ))

                            .then(CommandManager.literal("info")
                                .then(CommandManager.argument("name/id", StringArgumentType.string())
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
                                            context.source.sendMessage(Text.literal("§8» §7Action Info§8:"))
                                            context.source.sendMessage(Text.literal("§8» §7Name§8: §7${action.name}"))
                                            context.source.sendMessage(Text.literal("§8» §7ID§8: §7${action.id}"))
                                            context.source.sendMessage(Text.literal("§8» §7Triggers§8: §7${action.triggers.joinToString(", ")}"))
                                            context.source.sendMessage(Text.literal("§8» §7Author§8: §7${action.author}"))
                                        } else
                                            context.source.sendMessage(Text.literal("§8» §7Action $nameID not found."))

                                        Command.SINGLE_SUCCESS
                                    })))

                        .then(CommandManager.literal("list")
                            .executes { context ->
                                context.source.sendMessage(Text.literal("§8» §7Actions§8[§7${ActionManager.instance.actions.size}§8]:"))

                                for (action in ActionManager.instance.actions)
                                    context.source.sendMessage(Text.literal("§8» §7${action.name}§8[§7#${action.id}§8]"))

                                Command.SINGLE_SUCCESS
                            }))
            }
        }
    }
}
