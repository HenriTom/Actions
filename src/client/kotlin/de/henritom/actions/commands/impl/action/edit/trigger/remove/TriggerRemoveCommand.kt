package de.henritom.actions.commands.impl.action.edit.trigger.remove

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TriggerRemoveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("edit")
            .then(ClientCommandManager.argument("triggerID", IntegerArgumentType.integer(1))
                .suggests { context, builder ->
                    val nameID = StringArgumentType.getString(context, "name/id")
                    ActionManager.instance.getActionByNameID(nameID)?.triggers?.forEach { trigger ->
                        if (trigger.type != TriggerEnum.CALL)
                            builder.suggest(trigger.id)
                    }
                    builder.buildFuture()
                }
                .then(
                    ClientCommandManager.argument("triggerValue", StringArgumentType.string())
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
                        }
                )
            )
    }
}