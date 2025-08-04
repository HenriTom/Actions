package me.henritom.actionsv2.command.impl

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.command.impl.debug.CalcCommand
import me.henritom.actionsv2.command.impl.debug.ForceTriggerCommand
import me.henritom.actionsv2.command.impl.debug.TimeConvertCommand
import me.henritom.actionsv2.command.impl.debug.VariableConvertCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object DebugCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("debug")
            .then(CalcCommand.register())
            .then(ForceTriggerCommand.register())
            .then(TimeConvertCommand.register())
            .then(VariableConvertCommand.register())
    }
}