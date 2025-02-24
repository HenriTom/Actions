package de.henritom.actions.commands.impl.region.add

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import de.henritom.actions.region.Region
import de.henritom.actions.region.RegionManager
import de.henritom.actions.util.MessageUtil
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.util.math.Vec3d

object RegionAddCommand {

    fun register(): LiteralArgumentBuilder<FabricClientCommandSource>? {
        return ClientCommandManager.literal("add")
            .then(ClientCommandManager.argument("name", StringArgumentType.string())
                .then(ClientCommandManager.argument("x1", IntegerArgumentType.integer())
                    .then(ClientCommandManager.argument("y1", IntegerArgumentType.integer())
                        .then(ClientCommandManager.argument("z1", IntegerArgumentType.integer())
                            .then(ClientCommandManager.argument("x2", IntegerArgumentType.integer())
                                .then(ClientCommandManager.argument("y2", IntegerArgumentType.integer())
                                    .then(ClientCommandManager.argument("z2", IntegerArgumentType.integer())
                                        .executes { context ->
                                            val name = StringArgumentType.getString(context, "name")

                                            val x1 = IntegerArgumentType.getInteger(context, "x1")
                                            val y1 = IntegerArgumentType.getInteger(context, "y1")
                                            val z1 = IntegerArgumentType.getInteger(context, "z1")
                                            val x2 = IntegerArgumentType.getInteger(context, "x2")
                                            val y2 = IntegerArgumentType.getInteger(context, "y2")
                                            val z2 = IntegerArgumentType.getInteger(context, "z2")

                                            val messageUtil = MessageUtil(null)

                                            if (!RegionManager.instance.regions.none { it.name == name }) {
                                                messageUtil.printTranslatable("actions.region.already_used", name)
                                                return@executes Command.SINGLE_SUCCESS
                                            }

                                            val region = Region(name, Vec3d(x1.toDouble(), y1.toDouble(), z1.toDouble()), Vec3d(x2.toDouble(), y2.toDouble(), z2.toDouble()))

                                            RegionManager.instance.addRegion(region)
                                            messageUtil.printTranslatable("actions.region.added", region.name)

                                            Command.SINGLE_SUCCESS
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