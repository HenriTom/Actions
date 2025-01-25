package de.henritom.actions.commands.impl.prefix.clear

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ClearCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("clear")
            .executes {
                ActionManager.instance.commandPrefix = ""
                MessageUtil().printTranslatable("actions.prefix.clear")
                Command.SINGLE_SUCCESS
            }
    }
}