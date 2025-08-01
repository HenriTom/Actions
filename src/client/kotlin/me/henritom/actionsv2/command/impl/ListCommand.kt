package me.henritom.actionsv2.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.axn.ActionManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object ListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("list")
            .executes { context ->

                context.source.sendFeedback(Text.translatable("actions.commands.list.title", ActionManager.loadedActions.size))

                for ((id, action) in ActionManager.loadedActions)
                    context.source.sendFeedback(Text.translatable("actions.commands.list.it", action.name, id))

                1
            }
    }
}