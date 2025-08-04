package me.henritom.actionsv2.command.impl.regions

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.regions.RegionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object RegionRemoveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("remove")
            .then(ClientCommandManager.argument("id", StringArgumentType.string())
                .suggests { _, builder ->
                    RegionManager.regs.keys.forEach(builder::suggest)
                    builder.buildFuture()
                }
                .executes { context ->
                    val id = StringArgumentType.getString(context, "id")

                    val region = RegionManager.getRegion(id)

                    if (region == null) {
                        context.source.sendError(Text.translatable("actions.commands.regions.not_found", id))
                        return@executes 0
                    }

                    context.source.sendFeedback(Text.translatable("actions.commands.regions.remove.success", region.id))
                    RegionManager.removeRegion(region)

                    1
                }
            )

    }
}