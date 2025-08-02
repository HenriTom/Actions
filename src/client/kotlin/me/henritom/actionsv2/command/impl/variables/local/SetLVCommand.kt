package me.henritom.actionsv2.command.impl.variables.local

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.serialization.json.JsonPrimitive
import me.henritom.actionsv2.variables.LocalVariableStorage
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object SetLVCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("set")
            .then(ClientCommandManager.argument("variable", StringArgumentType.string())
                .suggests {
                    context, builder ->
                    LocalVariableStorage.vars.forEach { variable ->
                        builder.suggest(variable.key)
                    }
                    builder.buildFuture()
                }
                .then(ClientCommandManager.argument("value", StringArgumentType.string())
                    .executes { context ->
                        val variable = StringArgumentType.getString(context, "variable")
                        val value = StringArgumentType.getString(context, "value")

                        LocalVariableStorage.setVariable(variable, JsonPrimitive(value))

                        context.source.sendFeedback(Text.translatable("actions.commands.variables.set", variable, value))

                        1
                    }
                )
            )
    }
}