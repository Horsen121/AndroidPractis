package com.androidlearn.beatbox

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

private const val SOUNDS_FOLDER = "sample_sounds"

class MainViewModel(
    private val application: Application,
    private val assets: AssetManager = application.assets
): AndroidViewModel(application) {
    private val player = MediaPlayer()
    var currentSound by mutableStateOf<Sound?>(null)
    val sounds: List<Sound>

    init {

        sounds = loadSounds()
    }

    fun play(sound: Sound) {
            player.reset()

            val afd: AssetFileDescriptor = assets.openFd(sound.assetPath)
            player.setDataSource(afd.fileDescriptor,afd.startOffset,afd.length)
            currentSound = sound
            player.prepare()
            player.start()
    }

    private fun loadSounds(): List<Sound> {
        val soundNames: Array<String>?
        try {
            soundNames = assets.list(SOUNDS_FOLDER)
        } catch (e: Exception) {
            return emptyList()
        }
        val sounds = mutableListOf<Sound>()
        soundNames?.forEach { filename ->
            val assetPath = "$SOUNDS_FOLDER/$filename"
            sounds.add(Sound(assetPath))
        }
        return sounds
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}