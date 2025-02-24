package de.henritom.actions.commands.impl.file.save.regions

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveRegionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("regions")
            .executes {
                ConfigManager().saveRegions()
                MessageUtil(null).printTranslatable("actions.file.save.regions")
                Command.SINGLE_SUCCESS
            }
    }
}