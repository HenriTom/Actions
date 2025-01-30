package de.henritom.actions.commands.impl.action.edit.trigger.remove

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TriggerRemoveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("remove")
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
                }
            )
    }
}