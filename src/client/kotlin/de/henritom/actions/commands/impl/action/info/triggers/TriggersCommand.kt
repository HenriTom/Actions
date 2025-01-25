package de.henritom.actions.commands.impl.action.info.triggers

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TriggersCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("triggers")
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
    }
}