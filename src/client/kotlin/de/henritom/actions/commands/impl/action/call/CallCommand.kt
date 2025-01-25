package de.henritom.actions.commands.impl.action.call

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.triggers.TriggerEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object CallCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("call")
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
                }
            )
    }
}