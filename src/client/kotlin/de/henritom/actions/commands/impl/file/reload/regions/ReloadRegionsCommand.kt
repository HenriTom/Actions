package de.henritom.actions.commands.impl.file.reload.regions

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ReloadRegionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("regions")
            .executes {
                ConfigManager().reloadRegions()
                MessageUtil(null).printTranslatable("actions.file.reloaded.regions")
                Command.SINGLE_SUCCESS
            }
    }
}