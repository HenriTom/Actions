package de.henritom.actions.commands.impl.region.list

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.region.RegionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object RegionListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("list")
            .executes {
                MessageUtil().printTranslatable("actions.region.list.title", RegionManager.instance.regions.size.toString())

                for (region in RegionManager.instance.regions)
                    MessageUtil().printTranslatable("actions.region.list.it", region.name, region.pos1.toString(), region.pos2.toString())

                Command.SINGLE_SUCCESS
            }
    }
}