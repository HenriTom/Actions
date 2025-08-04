package me.henritom.actionsv2.command.impl.file

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.file.save.SaveAllActionsCommand
import me.henritom.actionsv2.command.impl.file.save.SaveAllCommand
import me.henritom.actionsv2.command.impl.file.save.SaveAllRegionsCommand
import me.henritom.actionsv2.command.impl.file.save.SaveAllVariablesCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object SaveCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("save")
            .then(SaveAllActionsCommand.register())
            .then(SaveAllCommand.register())
            .then(SaveAllRegionsCommand.register())
            .then(SaveAllVariablesCommand.register())
    }
}