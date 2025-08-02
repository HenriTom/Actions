package me.henritom.actionsv2.command.impl.variables

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.variables.global.GetCommand
import me.henritom.actionsv2.command.impl.variables.global.ListGVCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object GlobalCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("global")
            .then(GetCommand.register())
            .then(ListGVCommand.register())
    }
}