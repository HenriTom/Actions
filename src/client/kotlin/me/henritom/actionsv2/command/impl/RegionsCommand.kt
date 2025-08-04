package me.henritom.actionsv2.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.regions.RegionRemoveCommand
import me.henritom.actionsv2.command.impl.regions.RegionsListCommand
import me.henritom.actionsv2.command.impl.regions.RegionsSetCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object RegionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("regions")
            .then(RegionRemoveCommand.register())
            .then(RegionsListCommand.register())
            .then(RegionsSetCommand.register())
    }
}