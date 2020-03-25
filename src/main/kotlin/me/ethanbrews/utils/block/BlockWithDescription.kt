package me.ethanbrews.utils.block

import net.minecraft.block.Block

interface BlockWithDescription {
    val descriptionKey: String get() = (this as Block).translationKey + ".desc"
}