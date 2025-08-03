package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.util.CalcUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object CalcCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("calc")
            .then(ClientCommandManager.argument("expression", StringArgumentType.string())
                .executes { context ->
                    val expression = StringArgumentType.getString(context, "expression")

                    CalcUtil.evaluateExpression(expression)

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.time_convert.success", expression, CalcUtil.evaluateExpression(expression)))

                    1
                }
            )
    }
}