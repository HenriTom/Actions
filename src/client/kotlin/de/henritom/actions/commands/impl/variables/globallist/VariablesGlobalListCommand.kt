package de.henritom.actions.commands.impl.variables.globallist

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.ActionsClient
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object VariablesGlobalListCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("globallist")
            .then(ClientCommandManager.argument("page", IntegerArgumentType.integer(1))
                .executes { context ->
                    val page = IntegerArgumentType.getInteger(context, "page")
                    val messageUtil = MessageUtil(null)

                    messageUtil.printTranslatable("actions.variables.list.global.title", ActionsClient.globalVariableStorage.update().variables.size.toString())

                    val variables = ActionsClient.globalVariableStorage.update().variables.toList()
                    val maxPage = (variables.size / 10) + 1

                    messageUtil.printTranslatable("actions.variables.list.global.page", page.toString(), maxPage.toString())

                    if (page > maxPage) {
                        messageUtil.printTranslatable("actions.variables.list.page_not_found", maxPage.toString())
                        return@executes Command.SINGLE_SUCCESS
                    }

                    val start = (page - 1) * 10
                    val end = start + 10

                    for (i in start until end) {
                        if (i >= variables.size)
                            break

                        val (name, value) = variables[i]

                        if (name.startsWith("color_"))
                            messageUtil.printTranslatable("actions.variables.list.it", name, value.toString() + "example")
                        else
                            messageUtil.printTranslatable("actions.variables.list.it", name, value.toString())
                    }

                    Command.SINGLE_SUCCESS
                }
            )
    }
}