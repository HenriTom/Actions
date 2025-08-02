package me.henritom.actionsv2.command.impl.variables.local

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.variables.LocalVariableStorage
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object GetLVCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("get")
            .then(ClientCommandManager.argument("variable", StringArgumentType.string())
                .suggests {
                    context, builder ->
                    LocalVariableStorage.vars.forEach { variable ->
                        builder.suggest(variable.key)
                    }
                    builder.buildFuture()
                }
                .executes { context ->
                    val variable = StringArgumentType.getString(context, "variable")

                    if (variable.startsWith("color_") || variable.startsWith("format_"))
                        context.source.sendFeedback(Text.translatable("actions.commands.list.it", variable, LocalVariableStorage.getVariable(variable).toString() + "example"))
                    else
                        context.source.sendFeedback(Text.translatable("actions.commands.list.it", variable, LocalVariableStorage.getVariable(variable).toString()))

                    1
                }
            )
    }
}