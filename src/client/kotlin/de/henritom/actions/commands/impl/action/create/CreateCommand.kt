package de.henritom.actions.commands.impl.action.create

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object CreateCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("create")
            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                .executes { context ->
                    val name = StringArgumentType.getString(context, "name")

                    val messageUtil = MessageUtil(null)

                    when (ActionManager.instance.createAction(name)) {
                        1 -> messageUtil.printTranslatable("actions.action.created", name)
                        2 -> messageUtil.printTranslatable("actions.action.already_used", name)
                        3 -> messageUtil.printTranslatable("actions.action.start_with_letter")
                        4 -> messageUtil.printTranslatable("actions.action.min_length")
                    }

                    Command.SINGLE_SUCCESS
                }
            )
    }
}