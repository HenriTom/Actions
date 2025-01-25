package de.henritom.actions.commands.impl.action.delete

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object DeleteCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("delete")
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
                }
            )
    }
}