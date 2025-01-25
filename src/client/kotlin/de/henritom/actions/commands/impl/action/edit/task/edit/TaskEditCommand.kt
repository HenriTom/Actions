package de.henritom.actions.commands.impl.action.edit.task.edit

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionEditManager
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

object TaskEditCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("remove")
            .then(
                ClientCommandManager.argument("taskID", IntegerArgumentType.integer(1))
                    .suggests { context, builder ->
                        val nameID = StringArgumentType.getString(context, "name/id")
                        ActionManager.instance.getActionByNameID(nameID)?.tasks?.forEach { task ->
                            builder.suggest(task.id)
                        }
                        builder.buildFuture()
                    }.executes { context ->
                        val nameID = StringArgumentType.getString(context, "name/id")
                        val taskID = IntegerArgumentType.getInteger(context, "taskID")
                        val action = ActionManager.instance.getActionByNameID(nameID)

                        if (action == null) {
                            MessageUtil().printTranslatable("actions.action.not_found", nameID)
                            return@executes Command.SINGLE_SUCCESS
                        }

                        val task = action.tasks.find { it.id == taskID }

                        if (task == null) {
                            MessageUtil().printTranslatable("actions.task.not_found", taskID.toString())
                            return@executes Command.SINGLE_SUCCESS
                        }

                        if (ActionEditManager.instance.removeTask(action, task))
                            MessageUtil().printTranslatable("actions.task.removed", task.type.name, task.id.toString(), action.name)
                        else
                            MessageUtil().printTranslatable("actions.task.not_found_in_action", task.type.name, action.name)

                        Command.SINGLE_SUCCESS
                    }
            )
    }
}