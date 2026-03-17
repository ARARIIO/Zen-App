package zentimer.app.domain

data class TimerState(
    val remainingSeconds: Int,
    val totalSeconds: Int,
    val isRunning: Boolean,
    val isBreak: Boolean,
    val currentTask: String?
) {
    val progress: Float
        get() = if (totalSeconds > 0) 1f - (remainingSeconds.toFloat() / totalSeconds) else 0f

    fun formattedTime(): String {
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }
}
