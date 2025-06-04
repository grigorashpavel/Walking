package ru.pasha.feature.home.internal.presentation

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.pasha.common.Text
import ru.pasha.common.format
import ru.pasha.feature.home.internal.di.HomeScope
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.scheduleAtFixedRate

@HomeScope
internal class TimerController @Inject constructor(private val context: Context) {

    private var timer: Timer? = null
    private var startTime = 0L
    private var accumulatedTime = 0L
    private var isPaused = false

    private val template = Text.Resource(ru.pasha.common.R.string.walking_app_timer)

    private val _time = MutableStateFlow(formatTime(ZERO_SECONDS, context))
    val time: StateFlow<String> = _time.asStateFlow()

    fun start() {
        if (timer != null) return

        timer = Timer().apply {
            scheduleAtFixedRate(ZERO_SECONDS, ONE_SECOND) {
                if (!isPaused) {
                    val currentTime = System.currentTimeMillis() - startTime + accumulatedTime
                    val formatted = formatTime(currentTime, context)
                    updateTime(formatted)
                }
            }
        }
        startTime = System.currentTimeMillis()
        isPaused = false
    }

    fun pause() {
        isPaused = true
        accumulatedTime += System.currentTimeMillis() - startTime
        timer?.cancel()
        timer = null
    }

    fun reset() {
        timer?.cancel()
        timer = null
        startTime = System.currentTimeMillis()
        accumulatedTime = 0L
        isPaused = false

        updateTime(formatTime(ZERO_SECONDS, context))
    }

    private fun formatTime(millis: Long, context: Context): String {
        val totalSeconds = millis / ONE_SECOND
        val hours = (totalSeconds / 3600).toString().format("%02d")
        val minutes = (totalSeconds % 3600 / 60).toString().format("%02d")
        val seconds = (totalSeconds % 60).toString().format("%02d")

        return template.format(context).toString()
            .format(
                hours,
                minutes,
                seconds
            )
    }

    private fun updateTime(newTime: String) {
        _time.update { newTime }
    }

    private companion object {
        const val ZERO_SECONDS = 0L
        const val ONE_SECOND = 1000L
    }
}
