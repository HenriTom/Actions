package me.henritom.actionsv2.command.impl.variables.local

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.variables.LocalVariableStorage
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object RemoveLVCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("remove")
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

                    LocalVariableStorage.removeVariable(variable)

                    context.source.sendFeedback(Text.translatable("actions.commands.variables.removed", variable))

                    1
                }
            )
    }
}