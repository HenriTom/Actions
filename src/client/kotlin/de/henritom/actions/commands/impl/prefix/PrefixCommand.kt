package de.henritom.actions.commands.impl.prefix

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.prefix.clear.ClearCommand
import de.henritom.actions.commands.impl.prefix.set.SetCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object PrefixCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("prefix")
            .then(ClearCommand.register())
            .then(SetCommand.register())
    }
}