package me.ethanbrews.utils.block.blocks

import io.netty.buffer.Unpooled
import me.ethanbrews.utils.block.BlockEntities
import me.ethanbrews.utils.block.entity.StorageNetworkBlockEntity
import me.ethanbrews.utils.block.entity.StorageNetworkControllerBlockEntity
import me.ethanbrews.utils.logger
import me.ethanbrews.utils.network.NetworkIdentifiers
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.IWorld
import net.minecraft.world.World

class StorageNetworkControllerBlock : VisibleBlockWithEntity(Settings.of(Material(
    MaterialColor.AIR,
    false,
    false,
    true,
    false,
false,
    false,
    false,
    PistonBehavior.IGNORE
))), StorageNetworkBlock {
    override val name = "StorageNetworkControllerBlock"
    override val blockEntityType = BlockEntities.STORAGE_CONTROLLER_ENTITY

    override fun createBlockEntity(view: BlockView?): BlockEntity? {
        return StorageNetworkControllerBlockEntity()
    }

    override fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?) {
        super<VisibleBlockWithEntity>.onBreak(world, pos, state, player)
        if (world!!.isClient)
            return
        (world.getBlockEntity(pos!!) as StorageNetworkControllerBlockEntity).updateNetwork(true)

    }

    override fun onPlaced(
        world: World?,
        pos: BlockPos?,
        state: BlockState?,
        placer: LivingEntity?,
        itemStack: ItemStack?
    ) {
        super.onPlaced(world, pos, state, placer, itemStack)
        if (world!!.isClient)
            return
        val te = (world.getBlockEntity(pos) as StorageNetworkControllerBlockEntity)
        te.updateNetwork()
    }

    companion object {
        fun getControllerPosForNetwork(world: World, startPos: BlockPos, disallowStartPos: Boolean = true): BlockPos? {
            val foundBlocks = ArrayList<BlockPos>()

            fun nextSearch(pos: BlockPos): BlockPos? {
                val connectedBlocks = StorageNetworkBlock.getSurroundingStorageNetworkBlocks(world, pos)
                for(blockPos in connectedBlocks) {
                    val block = world.getBlockState(blockPos).block
                    if (block is StorageNetworkControllerBlock && (!disallowStartPos || startPos != blockPos))
                        return blockPos
                }
                for(blockPos in connectedBlocks) {
                    if (!foundBlocks.contains(blockPos)) {
                        foundBlocks.add(blockPos)
                        val next = nextSearch(blockPos)
                        if (next != null)
                            return next
                    }
                }
                return null
            }

            return nextSearch(startPos)
        }

        fun getControllerBlockForNetwork(world: World, startPos: BlockPos, disallowStartPos: Boolean = true): StorageNetworkControllerBlock? {
            return world.getBlockState(getControllerPosForNetwork(world, startPos, disallowStartPos) ?: return null)?.block as StorageNetworkControllerBlock
        }

        fun getControllerBlockEntityForNetwork(world: World, startPos: BlockPos, disallowStartPos: Boolean = true): StorageNetworkControllerBlockEntity? {
            return world.getBlockEntity(getControllerPosForNetwork(world, startPos, disallowStartPos) ?: return null) as StorageNetworkControllerBlockEntity
        }

        fun getListOfEntitiesInNetwork(controller: StorageNetworkControllerBlockEntity): ArrayList<StorageNetworkBlockEntity> {
            val blocks = ArrayList<BlockPos>()

            fun addConnectedBlocksToList(startPos: BlockPos) {
                val nextBlocks = StorageNetworkBlock.getSurroundingStorageNetworkBlocks(controller.world!!, startPos)
                for(b in nextBlocks) {
                    if (!blocks.contains(b)) {
                        blocks.add(b)
                        addConnectedBlocksToList(b)
                    }
                }
            }

            addConnectedBlocksToList(controller.pos)

            val entities = ArrayList<StorageNetworkBlockEntity>()
            for(b in blocks) {
                val be = controller.world!!.getBlockEntity(b)
                if (be is StorageNetworkBlockEntity)
                    entities.add(be)
            }
            return entities
        }
    }
}