package de.henritom.actions.commands.impl.file.reload.config

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveConfigCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("config")
            .executes {
                ConfigManager().saveConfig()
                MessageUtil().printTranslatable("actions.file.save.config")
                Command.SINGLE_SUCCESS
            }
    }
}