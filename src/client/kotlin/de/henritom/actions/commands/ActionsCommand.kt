package de.henritom.actions.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager

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
                                            1 -> MessageUtil().sendChat("§8» §7Action '$actionName' called.")
                                            2 -> MessageUtil().sendChat("§8» §7Action '$nameID' not found.")
                                            3 -> MessageUtil().sendChat("§8» §7Action '$actionName' is not callable.")
                                        }

                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(CommandManager.literal("create")
                                .then(CommandManager.argument("name", StringArgumentType.string())
                                    .executes { context ->
                                        val name = StringArgumentType.getString(context, "name")

                                        when (ActionManager.instance.createAction(name)) {
                                            1 -> MessageUtil().sendChat("§8» §7Action '$name' created.")
                                            2 -> MessageUtil().sendChat("§8» §7Name '$name' already used.")
                                            3 -> MessageUtil().sendChat("§8» §7Name must start with a letter.")
                                            4 -> MessageUtil().sendChat("§8» §7Name must be at least 3 characters long.")
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

                                        val actionName = ActionManager.instance.getActionByNameID(nameID)?.name ?: nameID

                                        if (ActionManager.instance.deleteAction(nameID))
                                            MessageUtil().sendChat("§8» §7Action '$actionName' deleted.")

                                        else
                                            MessageUtil().sendChat("§8» §7Action '$nameID' not found.")

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
                                    .then(CommandManager.literal("trigger")
                                        .then(CommandManager.literal("add")
                                            .then(CommandManager.argument("trigger", StringArgumentType.string())
                                                .suggests { context, builder ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    val action = ActionManager.instance.getActionByNameID(nameID)

                                                    for (trigger in TriggerEnum.entries)
                                                        if (trigger != TriggerEnum.CALL || action?.triggers?.none { it.type == TriggerEnum.CALL } == true)
                                                            builder.suggest(trigger.name)

                                                    builder.buildFuture()
                                                }
                                                .then(CommandManager.argument("initValue", StringArgumentType.string())
                                                    .executes { context ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        val triggerName = StringArgumentType.getString(context, "trigger")
                                                        val initValue = StringArgumentType.getString(context, "initValue")
                                                        val action = ActionManager.instance.getActionByNameID(nameID)

                                                        if (action == null) {
                                                            MessageUtil().sendChat("§8» §7Action '$nameID' not found.")
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        val trigger = try {
                                                            TriggerEnum.valueOf(triggerName)
                                                        } catch (e: IllegalArgumentException) {
                                                            MessageUtil().sendChat("§8» §7Trigger '$triggerName' not found.")
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        if (ActionEditManager.instance.addTrigger(action, trigger) == 1) {
                                                            action.triggers.last().value = initValue

                                                            MessageUtil().sendChat("§8» §7Trigger '${trigger.name}' added to action $nameID with initial value: $initValue.")
                                                        } else if (ActionEditManager.instance.addTrigger(action, trigger) == 2)
                                                            MessageUtil().sendChat("§8» §7Can't add multiple call triggers.")

                                                        Command.SINGLE_SUCCESS
                                                    })
                                                .executes { context ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    val triggerName = StringArgumentType.getString(context, "trigger")
                                                    val action = ActionManager.instance.getActionByNameID(nameID)

                                                    if (action == null) {
                                                        MessageUtil().sendChat("§8» §7Action '$nameID' not found.")
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    val trigger = try {
                                                        TriggerEnum.valueOf(triggerName)
                                                    } catch (e: IllegalArgumentException) {
                                                        MessageUtil().sendChat("§8» §7Trigger '$triggerName' not found.")
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    if (ActionEditManager.instance.addTrigger(action, trigger) == 1)
                                                        MessageUtil().sendChat("§8» §7Trigger '${trigger.name}' added to action $nameID.")
                                                    else if (ActionEditManager.instance.addTrigger(action, trigger) == 2)
                                                        MessageUtil().sendChat("§8» §7Can't add multiple call triggers.")

                                                    Command.SINGLE_SUCCESS
                                                }))

                                        .then(CommandManager.literal("remove")
                                            .then(CommandManager.argument("triggerID", IntegerArgumentType.integer(1))
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
                                                        MessageUtil().sendChat("§8» §7Action '$nameID' not found.")
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    val trigger = action.triggers.find { it.id == triggerID }

                                                    if (trigger == null) {
                                                        MessageUtil().sendChat("§8» §7Trigger '$triggerID' not found.")
                                                        return@executes Command.SINGLE_SUCCESS
                                                    }

                                                    if (ActionEditManager.instance.removeTrigger(action, trigger))
                                                        MessageUtil().sendChat("§8» §7Trigger '${trigger.type.name}§8[§7${trigger.id}§8]§7' removed from action '${action.name}'.")
                                                    else
                                                        MessageUtil().sendChat("§8» §7Trigger '${trigger.type.name}' not found in action '${action.name}'.")

                                                    Command.SINGLE_SUCCESS
                                                }))

                                        .then(CommandManager.literal("edit")
                                            .then(CommandManager.argument("triggerID", IntegerArgumentType.integer(1))
                                                .suggests { context, builder ->
                                                    val nameID = StringArgumentType.getString(context, "name/id")
                                                    ActionManager.instance.getActionByNameID(nameID)?.triggers?.forEach { trigger ->
                                                        builder.suggest(trigger.id)
                                                    }
                                                    builder.buildFuture()
                                                }
                                                .then(CommandManager.argument("triggerValue", StringArgumentType.string())
                                                    .executes { context ->
                                                        val nameID = StringArgumentType.getString(context, "name/id")
                                                        val triggerID = IntegerArgumentType.getInteger(context, "triggerID")
                                                        val triggerValue = StringArgumentType.getString(context, "triggerValue")
                                                        val action = ActionManager.instance.getActionByNameID(nameID)

                                                        if (action == null) {
                                                            MessageUtil().sendChat("§8» §7Action '$nameID' not found.")
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        val trigger = action.triggers.find { it.id == triggerID }

                                                        if (trigger == null) {
                                                            MessageUtil().sendChat("§8» §7Trigger '$triggerID' not found.")
                                                            return@executes Command.SINGLE_SUCCESS
                                                        }

                                                        trigger.value = triggerValue
                                                        MessageUtil().sendChat("§8» §7Changed value of trigger '${trigger.type.name}§8[§7${trigger.id}§8]§7' to: $triggerValue.")

                                                        Command.SINGLE_SUCCESS
                                                    }))))

                                    .then(CommandManager.literal("removeauthor")
                                        .executes { context ->
                                            val nameID = StringArgumentType.getString(context, "name/id")
                                            val action = ActionManager.instance.getActionByNameID(nameID)

                                            if (action == null) {
                                                MessageUtil().sendChat("§8» §7Action '$nameID' not found.")
                                                return@executes Command.SINGLE_SUCCESS
                                            }

                                            if (ActionEditManager.instance.removeAuthor(action))
                                                MessageUtil().sendChat("§8» §7Author removed from action '${action.name}'.")
                                            else
                                                MessageUtil().sendChat("§8» §7Author not found in action '${action.name}'.")

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
                                            MessageUtil().sendChat("§8» §7Action Info§8:")
                                            MessageUtil().sendChat("§8» §7Name§8: §7'${action.name}'")
                                            MessageUtil().sendChat("§8» §7ID§8: §7'${action.id}'")
                                            MessageUtil().sendChat("§8» §7Author§8: §7'${action.author}'")
                                            MessageUtil().sendChat("§8» §7Triggers§8: §7'${action.triggers.size}'")
                                        } else
                                            MessageUtil().sendChat("§8» §7Action '$nameID' not found.")

                                        Command.SINGLE_SUCCESS
                                    }

                                    .then(CommandManager.literal("triggers")
                                        .executes { context ->
                                            val nameID = StringArgumentType.getString(context, "name/id")
                                            val action = ActionManager.instance.getActionByNameID(nameID)

                                            if (action != null) {
                                                MessageUtil().sendChat("§8» §7Triggers§8[§7${action.triggers.size}§8]:")

                                                for (trigger in action.triggers)
                                                    if (trigger.type == TriggerEnum.CALL)
                                                        MessageUtil().sendChat("§8» §7${trigger.type.name}§8[§7#${trigger.id}§8]")
                                                    else
                                                        MessageUtil().sendChat("§8» §7${trigger.type.name}§8(§7${trigger.value}§8)[§7#${trigger.id}§8]")
                                            }

                                            Command.SINGLE_SUCCESS
                                        }
                                    ))))

                        .then(CommandManager.literal("list")
                            .executes {
                                MessageUtil().sendChat("§8» §7Actions§8[§7${ActionManager.instance.actions.size}§8]:")

                                for (action in ActionManager.instance.actions)
                                    MessageUtil().sendChat("§8» §7${action.name}§8[§7#${action.id}§8]")

                                Command.SINGLE_SUCCESS
                            })

                        .then(CommandManager.literal("prefix")
                            .then(CommandManager.literal("set")
                                .then(CommandManager.argument("prefix", StringArgumentType.string())
                                    .executes { context ->
                                        val prefix = StringArgumentType.getString(context, "prefix")
                                        ActionManager.instance.commandPrefix = prefix
                                        MessageUtil().sendChat("§8» §7Command prefix set to '$prefix'.")
                                        Command.SINGLE_SUCCESS
                                    }))

                            .then(CommandManager.literal("clear")
                                .executes {
                                    ActionManager.instance.commandPrefix = ""
                                    MessageUtil().sendChat("§8» §7Command prefix got cleared.")
                                    Command.SINGLE_SUCCESS
                                })
                        )
                )
            }
        }
    }
}