package zentimer.app.domain

interface SoundPlayer {
    fun play(soundType: String)
    fun stop()
}
