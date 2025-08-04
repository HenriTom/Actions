package me.henritom.actionsv2.command.impl.file

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.file.load.LoadAllActionsCommand
import me.henritom.actionsv2.command.impl.file.load.LoadAllCommand
import me.henritom.actionsv2.command.impl.file.load.LoadAllRegionsCommand
import me.henritom.actionsv2.command.impl.file.load.LoadAllVariablesCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object LoadCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("load")
            .then(LoadAllActionsCommand.register())
            .then(LoadAllCommand.register())
            .then(LoadAllRegionsCommand.register())
            .then(LoadAllVariablesCommand.register())
    }
}