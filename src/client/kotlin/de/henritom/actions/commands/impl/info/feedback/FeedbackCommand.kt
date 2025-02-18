package de.henritom.actions.commands.impl.info.feedback

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object FeedbackCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("feedback")
            .executes {
                MessageUtil(null).printTranslatableClickable("actions.feedback", "https://github.com/HenriTom/Actions/issues/new/choose")

                Command.SINGLE_SUCCESS
            }
    }
}