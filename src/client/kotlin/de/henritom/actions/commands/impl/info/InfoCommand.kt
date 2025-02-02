package de.henritom.actions.commands.impl.info

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.commands.impl.info.version.*
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

class InfoCommand {
    companion object {
        fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
            return ClientCommandManager.literal("info")
                .then(DiscordCommand.register())
                .then(FeedbackCommand.register())
                .then(ModrinthCommand.register())
                .then(VersionCommand.register())
                .then(WikiCommand.register())
        }
    }
}
