package de.henritom.actions.commands.impl.list

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object ListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("list")
            .executes {
                MessageUtil().printTranslatable("actions.list.title", ActionManager.instance.actions.size.toString())

                for (action in ActionManager.instance.actions)
                    MessageUtil().printTranslatable("actions.list.it", action.name, action.id.toString())

                Command.SINGLE_SUCCESS
            }
    }
}