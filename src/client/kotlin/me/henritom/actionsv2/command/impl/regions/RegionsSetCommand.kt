package me.henritom.actionsv2.command.impl.regions

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import me.henritom.actionsv2.regions.Region
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object RegionsSetCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("set")
            .then(ClientCommandManager.argument("id", StringArgumentType.string())
                .then(ClientCommandManager.argument("x1", IntegerArgumentType.integer())
                    .then(ClientCommandManager.argument("y1", IntegerArgumentType.integer())
                        .then(ClientCommandManager.argument("z1", IntegerArgumentType.integer())
                            .then(ClientCommandManager.argument("x2", IntegerArgumentType.integer())
                                .then(ClientCommandManager.argument("y2", IntegerArgumentType.integer())
                                    .then(ClientCommandManager.argument("z2", IntegerArgumentType.integer())
                                        .executes {  context ->
                                            val id = StringArgumentType.getString(context, "id")
                                            val x1 = IntegerArgumentType.getInteger(context, "x1")
                                            val y1 = IntegerArgumentType.getInteger(context, "y1")
                                            val z1 = IntegerArgumentType.getInteger(context, "z1")
                                            val x2 = IntegerArgumentType.getInteger(context, "x2")
                                            val y2 = IntegerArgumentType.getInteger(context, "y2")
                                            val z2 = IntegerArgumentType.getInteger(context, "z2")
                                            val world = context.source.world.registryKey.value.toString()
                                            val server = context.source.player.server?.serverIp ?: "127.0.0.1"

                                            val region = Region(id, server, world, x1, y1, z1, x2, y2, z2)

                                            context.source.sendFeedback(Text.translatable("actions.commands.regions.set.success", id, region.server, region.world, region.x1, region.y1, region.z1, region.x2, region.y2, region.z2))

                                            1
                                        }
                                    )
                                )
                            )
                        )
                    )
                )
            )
    }
}