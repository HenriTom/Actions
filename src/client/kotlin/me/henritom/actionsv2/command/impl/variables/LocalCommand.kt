package me.henritom.actionsv2.command.impl.variables

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.variables.local.GetLVCommand
import me.henritom.actionsv2.command.impl.variables.local.ListLVCommand
import me.henritom.actionsv2.command.impl.variables.local.SetLVCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object LocalCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("local")
            .then(GetLVCommand.register())
            .then(ListLVCommand.register())
            .then(SetLVCommand.register())
    }
}