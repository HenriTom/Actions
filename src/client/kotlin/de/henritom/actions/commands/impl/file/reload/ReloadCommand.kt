package de.henritom.actions.commands.impl.file.reload

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.file.reload.actions.ReloadActionsCommand
import de.henritom.actions.commands.impl.file.reload.config.ReloadConfigCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ReloadCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("reload")
            .then(ReloadActionsCommand.register())
            .then(ReloadConfigCommand.register())
    }
}