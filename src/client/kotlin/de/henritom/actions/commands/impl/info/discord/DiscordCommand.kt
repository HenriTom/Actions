package de.henritom.actions.commands.impl.info.discord

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object DiscordCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("discord")
            .executes {
                MessageUtil(null).printTranslatableClickable("actions.discord", "https://discord.gg/XdHBJKTvxJ")

                Command.SINGLE_SUCCESS
            }
    }
}