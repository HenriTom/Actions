package me.henritom.actionsv2.command.impl.regions

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.regions.RegionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object RegionsListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("list")
            .then(ClientCommandManager.argument("page", IntegerArgumentType.integer(1))
                .executes { context ->
                    val page = IntegerArgumentType.getInteger(context, "page")

                    val regions = RegionManager.regs.toList()

                    context.source.sendFeedback(Text.translatable("actions.commands.regions.title", regions.size))

                    val maxPage = (regions.size / 10) + 1

                    context.source.sendFeedback(Text.translatable("actions.commands.variables.page", page.toString(), maxPage.toString()))

                    if (page > maxPage) {
                        context.source.sendError(Text.translatable("actions.commands.variables.page_not_found", page.toString()))
                        return@executes 0
                    }

                    val start = (page - 1) * 10
                    val end = start + 10

                    for (i in start until end) {
                        if (i >= regions.size)
                            break

                        val (id, region) = regions[i]

                        context.source.sendFeedback(Text.translatable("actions.commands.regions.list.it", id, region.server, region.world.toString(), region.x1, region.y1, region.z1, region.x2, region.y2, region.z2))
                    }

                    1
                }
            )
    }
}