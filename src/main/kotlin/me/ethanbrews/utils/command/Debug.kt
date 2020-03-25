package me.ethanbrews.utils.command


import com.mojang.brigadier.arguments.StringArgumentType
import net.fabricmc.fabric.api.registry.CommandRegistry
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier
import me.ethanbrews.utils.config.Config
import me.ethanbrews.utils.config.commands
import me.ethanbrews.utils.modid

object Debug : ICommandLoader {
    override fun canLoad() = Config[commands.debug]

    override fun load() {
        CommandRegistry.INSTANCE.register(false) { dispatcher ->
            dispatcher.register(
                literal(modid).then(
                    literal("resource").then(
                        argument("name", StringArgumentType.greedyString()).executes { context ->
                            Identifier.tryParse(StringArgumentType.getString(context, "name"))?.let { id ->
                                try {
                                    context.source.minecraftServer.dataManager.getResource(id).use { resource ->
                                        resource.inputStream.use { input ->
                                            input.bufferedReader().lines().forEach {
                                                context.source.sendFeedback(LiteralText(it), false)
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    context.source.sendError(LiteralText(e.message))
                                    e.printStackTrace()
                                }
                            }
                            0
                        }
                    )
                )
            )
        }
    }
}