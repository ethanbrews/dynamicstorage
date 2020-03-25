package me.ethanbrews.utils.block.blocks

import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface StorageNetworkBlock {

    val name: String

    fun onBreak(world: World?, pos: BlockPos?, state: BlockState?, player: PlayerEntity?) {
        StorageNetworkControllerBlock.getControllerBlockEntityForNetwork(world!!, pos!!, true)?.validateNetwork()
    }

    companion object {
        fun getSurroundingStorageNetworkBlocks(world: World, startPos: BlockPos): ArrayList<BlockPos> {
            val blocks = ArrayList<BlockPos>()
            for(pos in arrayOf(
                startPos.up(),
                startPos.down(),
                startPos.north(),
                startPos.east(),
                startPos.south(),
                startPos.west()
            )) {
                if (world.getBlockState(pos).block is StorageNetworkBlock)
                    blocks.add(pos)
            }
            return blocks
        }
    }
}