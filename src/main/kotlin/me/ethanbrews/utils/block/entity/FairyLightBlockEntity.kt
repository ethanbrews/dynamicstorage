package me.ethanbrews.utils.block.entity

import me.ethanbrews.utils.block.BlockEntities
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.math.BlockPos


class FairyLightBlockEntity : BlockEntity(BlockEntities.FAIRY_LIGHT_BLOCK_ENTITY) {

    // We will render the wire to this block in our FairyLightRenderer.render method
    var connectedTo: FairyLightBlockEntity? = null;
    // We only store this so we can tell this block when we get broken
    var connectedFrom: FairyLightBlockEntity? = null;

    override fun fromTag(tag: CompoundTag?) {
        super.fromTag(tag)
        val posTo: IntArray = tag?.getIntArray("connectedTo") ?: return
        connectedTo = world?.getBlockEntity(BlockPos(posTo[0], posTo[1], posTo[2])) as FairyLightBlockEntity?

        val posFrom: IntArray = tag.getIntArray("connectedFrom") ?: return
        connectedFrom = world?.getBlockEntity(BlockPos(posFrom[0], posFrom[1], posFrom[2])) as FairyLightBlockEntity?

    }

    override fun toTag(tag: CompoundTag): CompoundTag? {
        super.toTag(tag)
        // Save the current value of the number to the tag
        with(connectedTo!!.pos) {
            tag.putIntArray("connectedTo", intArrayOf(x, y, z))
        }
        with(connectedFrom!!.pos) {
            tag.putIntArray("connectedFrom", intArrayOf(x, y, z))
        }

        return tag
    }
}