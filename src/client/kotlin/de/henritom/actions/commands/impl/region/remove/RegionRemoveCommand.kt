package de.henritom.actions.commands.impl.region.remove

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.region.RegionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object RegionRemoveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("remove")
            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                .suggests { _, builder ->
                    RegionManager.instance.regions.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val name = StringArgumentType.getString(context, "name")
                    val region = RegionManager.instance.regionByName(name)

                    if (region == null) {
                        MessageUtil().printTranslatable("actions.region.not_found", name)
                        return@executes Command.SINGLE_SUCCESS
                    }

                    if (RegionManager.instance.removeRegion(region))
                        MessageUtil().printTranslatable("actions.region.removed", region.name)

                    else
                        MessageUtil().printTranslatable("actions.action.not_found", name)

                    Command.SINGLE_SUCCESS
                })
    }
}