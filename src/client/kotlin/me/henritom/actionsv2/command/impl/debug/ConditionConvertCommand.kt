package me.henritom.actionsv2.command.impl.debug

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.util.ConditionUtil
import me.henritom.actionsv2.variables.VariableHelper
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object ConditionConvertCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("condition_convert")
            .then(ClientCommandManager.argument("condition", StringArgumentType.string())
                .executes { context ->
                    val condition = StringArgumentType.getString(context, "condition")

                    context.source.sendFeedback(Text.translatable("actions.commands.debug.time_convert.success", VariableHelper.replaceStr(condition, null), ConditionUtil.evaluateCondition(condition)))

                    1
                }
            )
    }
}