package tech.aliorpse.api.shared.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.time.Duration.Companion.milliseconds

object TaskScheduler {
    private val logger = logger()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun new(hour: Int = 0, minute: Int = 0, name: String, block: suspend () -> Unit): Job {
        return scope.launch(start = CoroutineStart.LAZY) {
            while (isActive) {
                val now = System.currentTimeMillis()
                val target = calculateNextRunTime(now, hour, minute)
                val delayTime = target - now

                logger.info("Task $name is delaying for ${delayTime.milliseconds}.")

                delay(delayTime)

                withContext(Dispatchers.Default) {
                    runCatching {
                        block()
                    }.onFailure {
                        logger.error("Task execution failed", it)
                    }
                }

                delay(1000)
            }
        }
    }

    private fun calculateNextRunTime(now: Long, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return calendar.timeInMillis
    }
}
