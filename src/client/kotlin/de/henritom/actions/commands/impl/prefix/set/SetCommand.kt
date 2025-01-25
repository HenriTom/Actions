package de.henritom.actions.commands.impl.prefix.set

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SetCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("set")
            .then(
                ClientCommandManager.argument("prefix", StringArgumentType.string())
                    .executes { context ->
                        val prefix = StringArgumentType.getString(context, "prefix")
                        ActionManager.instance.commandPrefix = prefix
                        MessageUtil().printTranslatable("actions.prefix.set", prefix)
                        Command.SINGLE_SUCCESS
                    }
            )
    }
}