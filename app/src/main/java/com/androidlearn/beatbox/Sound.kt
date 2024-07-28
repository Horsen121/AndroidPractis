package com.androidlearn.beatbox

private const val MP3 = ".mp3"

data class Sound(
    val assetPath: String,
    val name: String = assetPath.split("/").last().removeSuffix(MP3)
)
