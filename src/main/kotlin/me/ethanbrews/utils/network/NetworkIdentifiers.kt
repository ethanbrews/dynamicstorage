package me.ethanbrews.utils.network

import me.ethanbrews.utils.block.blocks.StorageNetworkControllerBlock
import me.ethanbrews.utils.block.entity.StorageNetworkControllerBlockEntity
import me.ethanbrews.utils.logger
import me.ethanbrews.utils.modid
import net.fabricmc.fabric.api.client.render.ColorProviderRegistry
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.PacketContext
import net.minecraft.client.MinecraftClient
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.Identifier
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos


object NetworkIdentifiers {
    var UPDATE_FAIRY_LIGHT_TILE_ENTITY = Identifier(modid, "update-flte")
    var UPDATE_STORAGE_NETWORK_CONTROLLER = Identifier(modid, "update-storage-network-controller")

    fun init() {

    }

    fun initClient() {
        ClientSidePacketRegistry.INSTANCE.register(
            UPDATE_FAIRY_LIGHT_TILE_ENTITY
        ) { packetContext: PacketContext?, attachedData: PacketByteBuf? ->
            val pos: BlockPos = attachedData!!.readBlockPos()
            packetContext?.taskQueue?.execute {
                MinecraftClient.getInstance().particleManager.addParticle(
                    ParticleTypes.CLOUD, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(),
                    0.0, 0.0, 0.0
                );
            }
        }

        ClientSidePacketRegistry.INSTANCE.register(UPDATE_STORAGE_NETWORK_CONTROLLER){ context, buf ->
            // Deserialize
            val pos = buf.readBlockPos()
            val isNetworkValid = buf.readBoolean()
            context.taskQueue.execute {

                val te = MinecraftClient.getInstance().world!!.getBlockEntity(pos)
                if (te !is StorageNetworkControllerBlockEntity)
                    logger.error("The block entity at ${pos.toShortString()} is not an instance of StorageNetworkControllerBlockEntity")
                else {
                    te.isNetworkValid = isNetworkValid
                    logger.info("Update block entity with new data from packet")
                    // We are running on the client thread here
                    //MinecraftClient.getInstance().world?.updateHorizontalAdjacent(pos.west(), MinecraftClient.getInstance().world?.getBlockState(pos.west())?.block)

                }


            }
        }
    }

    fun initServer() {

    }
}