package de.henritom.actions.commands.impl.version

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader

object VersionCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("version")
            .executes {
                MessageUtil().printTranslatable("actions.version", FabricLoader.getInstance().getModContainer("actions").get().metadata.version.toString())

                Command.SINGLE_SUCCESS
            }
    }
}