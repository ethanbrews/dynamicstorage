package me.ethanbrews.utils.item

import net.minecraft.block.Block
import net.minecraft.item.ItemGroup

class DefaultBlockItem(block: Block) : BlockItemBase(block, Settings().group(ItemGroup.MISC)) {
}