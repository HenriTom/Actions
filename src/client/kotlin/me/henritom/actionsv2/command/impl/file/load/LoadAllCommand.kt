package me.henritom.actionsv2.command.impl.file.load

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.loader.ActionsLoader
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object LoadAllCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("all")
            .then(ClientCommandManager.argument("includeDuplicates", BoolArgumentType.bool())
                .executes { context ->
                    val includeDuplicates = BoolArgumentType.getBool(context, "includeDuplicates")

                    ActionsLoader.loadAll(!includeDuplicates)

                    if (includeDuplicates)
                        context.source.sendFeedback(Text.translatable("actions.commands.file.load.all_duplicates.success"))
                    else
                        context.source.sendFeedback(Text.translatable("actions.commands.file.load.all.success"))

                    1
                }
            )
            .executes { context ->
                ActionsLoader.loadAll()

                context.source.sendFeedback(Text.translatable("actions.commands.file.load.all.success"))

                1
            }
    }
}