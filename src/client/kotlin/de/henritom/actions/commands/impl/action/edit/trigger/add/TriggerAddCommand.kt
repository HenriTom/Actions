package de.henritom.actions.commands.impl.action.edit.trigger.add

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.triggers.settings.InvUpdateEnum
import de.henritom.actions.triggers.settings.ReceiveMessageEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TriggerAddCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("add")
            .then(ClientCommandManager.argument("trigger", StringArgumentType.string())
                .suggests { context, builder ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    val action = ActionManager.instance.getActionByNameID(nameID)

                    for (trigger in TriggerEnum.entries)
                        if (trigger != TriggerEnum.CALL || action?.triggers?.none { it.type == TriggerEnum.CALL } == true)
                            builder.suggest(trigger.name)

                    builder.buildFuture()
                }
                .then(
                    ClientCommandManager.argument("initValue", StringArgumentType.string())
                        .suggests { context, builder ->
                            val trigger = try {
                                TriggerEnum.valueOf(StringArgumentType.getString(context, "trigger"))
                            } catch (e: IllegalArgumentException) {
                                builder.buildFuture()
                            }

                            if (trigger == TriggerEnum.RECEIVE_MESSAGE)
                                for (receiveType in ReceiveMessageEnum.entries)
                                    if (receiveType != ReceiveMessageEnum.ANY)
                                        builder.suggest("\"${receiveType.name}-")
                                    else
                                        builder.suggest(receiveType.name)

                            if (trigger == TriggerEnum.INV_UPDATE)
                                for (updateType in InvUpdateEnum.entries)
                                    if (updateType != InvUpdateEnum.ANY)
                                        builder.suggest("\"${updateType.name}-")
                                    else
                                        builder.suggest(updateType.name)

                            builder.buildFuture()
                        }
                        .executes { context ->
                            val nameID = StringArgumentType.getString(context, "name/id")
                            val triggerName = StringArgumentType.getString(context, "trigger")
                            val initValue = StringArgumentType.getString(context, "initValue")
                            val action = ActionManager.instance.getActionByNameID(nameID)

                            val messageUtil = MessageUtil(action)

                            if (action == null) {
                                messageUtil.printTranslatable("actions.action.not_found", nameID)
                                return@executes Command.SINGLE_SUCCESS
                            }

                            val trigger = try {
                                TriggerEnum.valueOf(triggerName)
                            } catch (e: IllegalArgumentException) {
                                messageUtil.printTranslatable("actions.trigger.not_found", triggerName)
                                return@executes Command.SINGLE_SUCCESS
                            }

                            if (ActionEditManager.instance.addTrigger(action, trigger) == 1) {
                                action.triggers.last().value = initValue

                                messageUtil.printTranslatable("actions.trigger.added.initial_value", trigger.name, nameID, initValue)
                            } else if (ActionEditManager.instance.addTrigger(action, trigger) == 2)
                                messageUtil.printTranslatable("actions.trigger.multiple_triggers")

                            Command.SINGLE_SUCCESS
                        })
                .executes { context ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    val triggerName = StringArgumentType.getString(context, "trigger")
                    val action = ActionManager.instance.getActionByNameID(nameID)

                    val messageUtil = MessageUtil(action)

                    if (action == null) {
                        messageUtil.printTranslatable("actions.action.not_found", nameID)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    val trigger = try {
                        TriggerEnum.valueOf(triggerName)
                    } catch (e: IllegalArgumentException) {
                        messageUtil.printTranslatable("actions.trigger.not_found", triggerName)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    if (ActionEditManager.instance.addTrigger(action, trigger) == 1)
                        messageUtil.printTranslatable("actions.trigger.added", trigger.name, nameID)
                    else if (ActionEditManager.instance.addTrigger(action, trigger) == 2)
                        messageUtil.printTranslatable("actions.trigger.multiple_triggers")

                    Command.SINGLE_SUCCESS
                }
            )
    }
}