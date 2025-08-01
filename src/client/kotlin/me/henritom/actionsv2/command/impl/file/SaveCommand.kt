package me.henritom.actionsv2.command.impl.file

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.file.save.SaveAllCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("save")
            .then(SaveAllCommand.register())
    }
}