package de.henritom.actions.commands.impl.file.save

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.file.reload.actions.SaveActionsCommand
import de.henritom.actions.commands.impl.file.reload.config.SaveConfigCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("save")
            .then(SaveActionsCommand.register())
            .then(SaveConfigCommand.register())
    }
}