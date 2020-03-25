package me.ethanbrews.utils.item

import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item

open class BlockItemBase(block: Block, settings: Item.Settings) : BlockItem(block, settings) {
    //override fun isIn(group: ItemGroup?) = super.isIn(group) || group === AdornItems.GROUP
}