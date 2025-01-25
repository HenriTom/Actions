package de.henritom.actions.commands.impl.file

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.commands.impl.file.reload.ReloadCommand
import de.henritom.actions.commands.impl.file.save.SaveCommand
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object FileCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("file")
            .then(ReloadCommand.register())
            .then(SaveCommand.register())
    }
}