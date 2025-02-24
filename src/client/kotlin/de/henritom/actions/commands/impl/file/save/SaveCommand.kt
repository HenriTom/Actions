package de.henritom.actions.commands.impl.file.save

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.file.save.actions.SaveActionsCommand
import de.henritom.actions.commands.impl.file.save.config.SaveConfigCommand
import de.henritom.actions.commands.impl.file.save.regions.SaveRegionsCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("save")
            .then(SaveActionsCommand.register())
            .then(SaveConfigCommand.register())
            .then(SaveRegionsCommand.register())
    }
}