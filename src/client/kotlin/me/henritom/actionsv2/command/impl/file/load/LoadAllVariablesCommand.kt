package me.henritom.actionsv2.command.impl.file.load

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.variables.LocalVariableStorage
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object LoadAllVariablesCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("variables")
            .executes { context ->
                LocalVariableStorage.loadVariables()

                context.source.sendFeedback(Text.translatable("actions.commands.file.load.all_variables.success"))

                1
            }
    }
}