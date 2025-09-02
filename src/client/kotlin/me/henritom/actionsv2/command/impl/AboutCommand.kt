package me.henritom.actionsv2.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.about.AboutActionCommand
import me.henritom.actionsv2.command.impl.about.AboutTaskCommand
import me.henritom.actionsv2.command.impl.about.AboutTriggerCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object AboutCommand {
    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("about")
            .then(AboutActionCommand.register())
            .then(AboutTriggerCommand.register())
            .then(AboutTaskCommand.register())
    }
}