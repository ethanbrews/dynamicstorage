package me.ethanbrews.utils.lib

fun colour(rgb: Int, alpha: Int = 0xFF) =
    alpha shl 24 or rgb

fun colour(rgb: Int, alpha: Float) = colour(rgb, (alpha * 255f).toInt())