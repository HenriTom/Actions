package de.henritom.actions.commands.impl.region

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.region.add.RegionAddCommand
import de.henritom.actions.commands.impl.region.list.RegionListCommand
import de.henritom.actions.commands.impl.region.remove.RegionRemoveCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object RegionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("region")
            .then(RegionAddCommand.register())
            .then(RegionListCommand.register())
            .then(RegionRemoveCommand.register())
    }
}