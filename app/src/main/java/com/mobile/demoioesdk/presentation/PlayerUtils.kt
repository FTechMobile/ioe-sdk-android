package com.mobile.demoioesdk.presentation

import android.media.MediaPlayer

object PlayerUtils {
    private var player: MediaPlayer? = null

    fun playAudio(path: String) {
        try {
            player = MediaPlayer()
            if (player?.isPlaying == true) {
                player?.stop()
                player?.reset()
            }

            player?.setDataSource(path)
            player?.prepare()
            player?.start()

            player?.setOnCompletionListener {
                it.release()
                player = null
            }

            player?.isLooping = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
