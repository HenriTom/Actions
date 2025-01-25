package de.henritom.actions.commands.impl.file.reload.actions

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveActionsCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("actions")
            .executes {
                ConfigManager().saveAllActions()
                MessageUtil().printTranslatable("actions.file.save.actions")
                Command.SINGLE_SUCCESS
            }
    }
}