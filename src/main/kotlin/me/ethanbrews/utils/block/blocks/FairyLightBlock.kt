package me.ethanbrews.utils.block.blocks

import io.netty.buffer.Unpooled
import me.ethanbrews.utils.block.BlockEntities
import me.ethanbrews.utils.block.entity.FairyLightBlockEntity
import me.ethanbrews.utils.logger
import me.ethanbrews.utils.network.NetworkIdentifiers
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.fabricmc.fabric.api.server.PlayerStream
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.PacketByteBuf
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import java.util.*


val EntityBlockMappings = HashMap<PlayerEntity, FairyLightBlockEntity>()

class FairyLightBlock : VisibleBlockWithEntity(Block.Settings.copy(Blocks.FURNACE)) {
    override val blockEntityType = BlockEntities.FAIRY_LIGHT_BLOCK_ENTITY



    override fun onPlaced(world: World, pos: BlockPos, state: BlockState, entity: LivingEntity?, stack: ItemStack?) {
        if (entity is PlayerEntity) {
            val be = world.getBlockEntity(pos) as? FairyLightBlockEntity ?: return
        }
        logger.info("Placed FairyLightBlock")
    }

    override fun createBlockEntity(blockView: BlockView?): BlockEntity? {
        return FairyLightBlockEntity()
    }


    @Environment(EnvType.SERVER)
    private fun sendMessageFromServer() {
        /*
        player.server!!.playerManager.getPlayer(player.uuid)!!.networkHandler.sendPacket(
            TitleS2CPacket(
                type,
                Texts.parse(source, title, serverPlayerEntity, 0)
            )
        )
        */
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (world.isClient)
            return ActionResult.SUCCESS

        val te = world.getBlockEntity(pos)

        if (te !is FairyLightBlockEntity)
            return ActionResult.SUCCESS


        if (EntityBlockMappings.containsKey(player)) { // The player has previously selected a block
            if (EntityBlockMappings[player] === te) { // Cancel selection
                EntityBlockMappings.remove(player)
                logger.info("Connection cancelled (attempted to connect a block to itself)")
            } else {
                logger.info("Connection made from ${EntityBlockMappings[player]!!.pos.toShortString()} to ${te.pos.toShortString()}")
                te.connectedTo = EntityBlockMappings[player]
                EntityBlockMappings[player]!!.connectedFrom = te
                te.markDirty()
                EntityBlockMappings[player]!!.markDirty()

                // Send data to clients
                val watchingPlayers = PlayerStream.watching(world,pos)
                val passedData = PacketByteBuf(Unpooled.buffer())
                passedData.writeBlockPos(te.pos);
                passedData.writeBlockPos(EntityBlockMappings[player]!!.pos);
                watchingPlayers.forEach { p ->
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                        p,
                        NetworkIdentifiers.UPDATE_FAIRY_LIGHT_TILE_ENTITY,
                        passedData
                    )
                }

                // 'deselect' the block
                EntityBlockMappings.remove(player)
            }
        } else {
            logger.info("Begin connection")
            EntityBlockMappings[player] = te
        }
        return ActionResult.SUCCESS
    }
}