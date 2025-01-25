package de.henritom.actions.commands.impl.action.info.tasks

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.tasks.TaskEnum
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TasksCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("tasks")
            .executes { context ->
                val nameID = StringArgumentType.getString(context, "name/id")
                val action = ActionManager.instance.getActionByNameID(nameID)

                if (action != null) {
                    MessageUtil().printTranslatable("actions.action.tasks.title", action.tasks.size.toString())
                    MessageUtil().printTranslatable("actions.action.tasks.it.first")

                    for (task in action.tasks)
                        if (task.type == TaskEnum.COMMENT)
                            MessageUtil().printTranslatable("actions.action.tasks.it.comment", task.value.toString(), task.id.toString())
                        else
                            MessageUtil().printTranslatable("actions.action.tasks.it.other", task.type.name, task.value.toString(), task.id.toString())

                    MessageUtil().printTranslatable("actions.action.tasks.it.last")
                }

                Command.SINGLE_SUCCESS
            }
    }
}