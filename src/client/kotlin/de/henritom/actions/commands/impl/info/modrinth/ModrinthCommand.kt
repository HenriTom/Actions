package de.henritom.actions.commands.impl.info.modrinth

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ModrinthCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("modrinth")
            .executes {
                MessageUtil().printTranslatableClickable("actions.modrinth", "https://modrinth.com/mod/actions")

                Command.SINGLE_SUCCESS
            }
    }
}