package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.util.RegionUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object CurrentRegionCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("current_region")
            .executes { context ->
                context.source.sendFeedback(Text.translatable("actions.commands.debug.current_region.success", RegionUtil.getCurrentRegions().joinToString(", ")))

                1
            }
    }
}