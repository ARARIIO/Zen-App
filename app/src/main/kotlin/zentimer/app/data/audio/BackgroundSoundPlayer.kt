package zentimer.app.data.audio

import android.content.Context
import android.media.MediaPlayer
import zentimer.app.data.preferences.UserPreferencesRepository
import zentimer.app.domain.SoundPlayer
import java.io.IOException

class BackgroundSoundPlayer(
    private val context: Context
) : SoundPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(soundType: String) {
        stop()
        if (soundType == UserPreferencesRepository.SOUND_NONE) return
        try {
            // Звуковые файлы можно добавить в res/raw — заглушка
            val resId: Int? = when (soundType) {
                UserPreferencesRepository.SOUND_NATURE -> null
                UserPreferencesRepository.SOUND_SPACE -> null
                UserPreferencesRepository.SOUND_WHITE_NOISE -> null
                else -> null
            }
            if (resId != null) {
                MediaPlayer.create(context, resId)?.let { mp ->
                    mediaPlayer = mp
                    mp.isLooping = true
                    mp.start()
                }
            }
        } catch (_: IOException) {
            // Звуковые файлы не добавлены
        }
    }

    override fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
    }
}
