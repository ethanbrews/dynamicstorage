package me.ethanbrews.utils.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import me.ethanbrews.utils.block.blocks.StorageNetworkControllerBlock
import me.ethanbrews.utils.block.entity.StorageNetworkBlockEntity
import me.ethanbrews.utils.config.Config
import me.ethanbrews.utils.config.commands.storage_network_debug
import me.ethanbrews.utils.logger
import me.ethanbrews.utils.modid
import net.fabricmc.fabric.api.registry.CommandRegistry
import net.minecraft.network.MessageType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object StorageNetworkDebug : ICommandLoader {
    override fun canLoad(): Boolean {
        return Config[storage_network_debug]
    }

    fun output(context: CommandContext<ServerCommandSource>, message: String) {
        context.source.player.sendChatMessage(LiteralText(message), MessageType.GAME_INFO)
        logger.info(message)
    }

    override fun load() {
        CommandRegistry.INSTANCE.register(false) { dispatcher ->
            dispatcher.register(
                CommandManager.literal(modid).then(
                    CommandManager.literal("netstat").then(
                        CommandManager.argument("xpos", IntegerArgumentType.integer()).then(
                            CommandManager.argument("ypos", IntegerArgumentType.integer()).then(
                                CommandManager.argument("zpos", IntegerArgumentType.integer()).executes { context ->
                                    IntegerArgumentType.getInteger(context, "xpos")?.let { xpos ->
                                        IntegerArgumentType.getInteger(context, "ypos")?.let { ypos ->
                                            IntegerArgumentType.getInteger(context, "zpos")?.let { zpos ->
                                                val pos = BlockPos(xpos, ypos, zpos)
                                                val be = context.source.world.getBlockEntity(pos)
                                                if (be is StorageNetworkBlockEntity) {
                                                    output(context, "The block entity at ${pos.toShortString()} is a StorageNetworkBlockEntity")
                                                    val controllerBE = StorageNetworkControllerBlock.getControllerBlockEntityForNetwork(context.source.world, pos, false)
                                                    if (controllerBE == null) {
                                                        output(context, "The network has no controller!")
                                                        return@executes 0
                                                    }
                                                    if (!controllerBE.isNetworkValid) {
                                                        output(context, "The network is invalid! (${if (controllerBE.invalidNetworkReason == null) "No reason specified" else controllerBE.invalidNetworkReason})")
                                                        return@executes 0
                                                    }
                                                    output(context, "== BEGIN ==")
                                                    output(context, "The network Controller is at ${controllerBE.pos.toShortString()}")
                                                    for(ent in controllerBE.networkBlockEntities) {
                                                        output(context, "   ${ent.name} at ${ent.pos.toShortString()}")
                                                    }
                                                    output(context, "=== END ===")
                                                }
                                            }
                                        }
                                    }
                                    0
                                }
                            )
                        )
                    )
                )
            )
        }
    }
}