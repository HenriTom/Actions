package me.henritom.actionsv2.command.impl.file.load

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.regions.RegionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object LoadAllRegionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("regions")
            .executes { context ->
                RegionManager.loadRegions()

                context.source.sendFeedback(Text.translatable("actions.commands.file.load.all_regions.success"))

                1
            }
    }
}