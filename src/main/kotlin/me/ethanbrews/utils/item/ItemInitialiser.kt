package me.ethanbrews.utils.item

import me.ethanbrews.utils.lib.RegistryHelper
import me.ethanbrews.utils.modid
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ItemInitialiser : RegistryHelper(modid) {
    val TEST_ITEM = registerItem("test_item", Item(Item.Settings().group(ItemGroup.MISC)))

    fun init() {

    }

    fun initClient() {

    }
}
