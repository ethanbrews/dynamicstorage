@file:UseSerializers(ForUuid::class, ForBlockPos::class)
package me.ethanbrews.utils.block.entity

import drawer.*
import io.netty.buffer.Unpooled
import me.ethanbrews.utils.block.BlockEntities
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket
import net.minecraft.nbt.CompoundTag
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import me.ethanbrews.utils.block.blocks.StorageNetworkBlock
import me.ethanbrews.utils.block.blocks.StorageNetworkControllerBlock
import me.ethanbrews.utils.logger
import me.ethanbrews.utils.network.NetworkIdentifiers
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos


class StorageNetworkControllerBlockEntity: StorageNetworkBlockEntity(BlockEntities.STORAGE_CONTROLLER_ENTITY) {

    override val name = "Controller"
    var isNetworkValid: Boolean = true
    var invalidNetworkReason: String? = null
    var networkBlockEntities = ArrayList<StorageNetworkBlockEntity>()

    override fun toTag(tag1: CompoundTag?): CompoundTag {
        val tag = toClientTag(tag1)
        return super.toTag(tag)
    }

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        fromClientTag(tag)
        updateNetwork()
    }

    override fun toClientTag(tag: CompoundTag?): CompoundTag {
        // Here we don't need to send as much info to the client as we save to the server's block state
        tag?.putBoolean("isNetworkValid", isNetworkValid)
        return tag ?: CompoundTag()
    }

    override fun fromClientTag(tag: CompoundTag?) {
        isNetworkValid = tag?.getBoolean("isNetworkValid") ?: true
    }

    /**
     * Ensures that only one controller is connected to the network using [networkBlockEntities]
     */
    fun validateNetwork() {
        var nOfControllers = 0
        var lastController: StorageNetworkControllerBlockEntity? = null
        for(e in networkBlockEntities) {
            if (e is StorageNetworkControllerBlockEntity) {
                nOfControllers++;
                if (lastController == null)
                    e.isNetworkValid = true
                else {
                    e.isNetworkValid = false
                    lastController.isNetworkValid = false
                }
                lastController = e
                e.markDirty()
                e.sync()
            }
        }
        if (nOfControllers == 1)
            logger.info("The network is valid with only 1 controller.")
        else
            logger.info("The network is invalid with $nOfControllers controllers.")
    }

    /**
     * Updates [networkBlockEntities] with a list of [StorageNetworkBlockEntity] connected to this controller
     *
     * @param ignoreThisBlockEntity Ignore the calling object when validating the network
     */
    fun updateNetwork(ignoreThisBlockEntity: Boolean = false) {
        // Traverse the network for block entities in the network
        networkBlockEntities = StorageNetworkControllerBlock.getListOfEntitiesInNetwork(this)
        if (ignoreThisBlockEntity)
            networkBlockEntities.remove(this)
        validateNetwork()
        logger.info("StorageNetworkControllerBlockEntity::updateNetwork() called")
    }

}