package de.henritom.actions.commands.impl.action.clone

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.actions.ActionManager
import de.henritom.actions.config.ConfigManager
import de.henritom.actions.util.MessageUtil
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import java.nio.file.Files

object CloneCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("clone")
            .then(ClientCommandManager.argument("name/id", StringArgumentType.string())
                .suggests { _, builder ->
                    ActionManager.instance.actions.forEach { action ->
                        builder.suggest(action.name)
                    }
                    builder.buildFuture()
                }

                .then(ClientCommandManager.argument("newName", StringArgumentType.string())
                    .executes { context ->
                        val name = StringArgumentType.getString(context, "name/id")
                        val newName = StringArgumentType.getString(context, "newName")

                        val action = ActionManager.instance.getActionByNameID(name)

                        if (action?.file == null) {
                            MessageUtil().printTranslatable("actions.action.not_found", name)
                            return@executes Command.SINGLE_SUCCESS
                        }

                        Files.copy(action.file!!.toPath(), action.file!!.parentFile.resolve("$newName.json").outputStream())
                        action.file!!.parentFile.resolve("$newName.json").writeText(Json.encodeToString(JsonObject(Json.parseToJsonElement(action.file!!.readText()).jsonObject.toMutableMap().apply { this["name"] = JsonPrimitive(newName) })))

                        ConfigManager().reloadActions()

                        Command.SINGLE_SUCCESS
                    }
                )
            )
    }
}