package de.henritom.actions.commands.impl.action.edit.removeauthor

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object RemoveAuthorCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("removeauthor")
            .executes { context ->
                val nameID = StringArgumentType.getString(context, "name/id")
                val action = ActionManager.instance.getActionByNameID(nameID)

                if (action == null) {
                    MessageUtil().printTranslatable("actions.action.not_found", nameID)
                    return@executes Command.SINGLE_SUCCESS
                }

                if (ActionEditManager.instance.removeAuthor(action))
                    MessageUtil().printTranslatable("actions.author.removed", action.name)
                else
                    MessageUtil().printTranslatable("actions.author.not_found", action.name)

                Command.SINGLE_SUCCESS
            }
    }
}