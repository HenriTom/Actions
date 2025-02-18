package de.henritom.actions.commands.impl.info.wiki

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object WikiCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("wiki")
            .executes {
                MessageUtil(null).printTranslatableClickable("actions.wiki", "https://github.com/HenriTom/Actions/wiki")

                Command.SINGLE_SUCCESS
            }
    }
}