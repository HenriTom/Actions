package me.henritom.actionsv2.command.impl.file.save

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.regions.RegionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SaveAllRegionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("regions")
            .executes { context ->
                RegionManager.saveRegions()

                context.source.sendFeedback(Text.translatable("actions.commands.file.save.all_regions.success"))

                1
            }
    }
}