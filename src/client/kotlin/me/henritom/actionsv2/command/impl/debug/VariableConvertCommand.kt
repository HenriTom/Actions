package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.variables.VariableHelper
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object VariableConvertCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("variable_convert")
            .then(ClientCommandManager.argument("variable", StringArgumentType.string())
                .executes { context ->
                    val variable = StringArgumentType.getString(context, "variable")

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.time_convert.success", variable, VariableHelper.replaceStr(variable)))

                    1
                }
            )
    }
}